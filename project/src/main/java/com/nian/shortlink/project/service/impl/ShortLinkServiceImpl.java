package com.nian.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nian.shortlink.project.common.convention.exception.ClientException;
import com.nian.shortlink.project.common.enums.VailDateTypeEnum;
import com.nian.shortlink.project.config.GotoDomainWhiteListConfiguration;
import com.nian.shortlink.project.domain.entity.*;
import com.nian.shortlink.project.domain.req.link.ShortLinkBatchCreateReqDTO;
import com.nian.shortlink.project.domain.req.link.ShortLinkCreateReqDTO;
import com.nian.shortlink.project.domain.req.link.ShortLinkPageDTO;
import com.nian.shortlink.project.domain.req.link.ShortLinkUpdateReqDTO;
import com.nian.shortlink.project.domain.resp.link.*;
import com.nian.shortlink.project.mapper.*;
import com.nian.shortlink.project.service.IShortLinkService;
import com.nian.shortlink.project.utils.HashUtil;
import com.nian.shortlink.project.utils.LinkUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.nian.shortlink.project.common.constat.RedisKeyConstant.*;
import static com.nian.shortlink.project.common.constat.ShortLinkConstant.AMAP_REMOTE_URL;
import static com.nian.shortlink.project.common.convention.errorcode.BaseErrorCode.CLIENT_ERROR;

/**
 * 短链接接口实现层
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLink> implements IShortLinkService {

    private final RBloomFilter<String> shortLinkCreateCacheBloomFilter;
    private final ShortLinkGotoMapper shortLinkGotoMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedissonClient redissonClient;
    private final LinkAccessMapper linkAccessMapper;
    private final LinkLocaleMapper linkLocaleMapper;
    private final LinkOsMapper linkOsMapper;
    private final LinkBrowserMapper linkBrowserMapper;
    private final LinkAccessLogsMapper linkAccessLogsMapper;
    private final LinkDeviceMapper linkDeviceMapper;
    private final LinkNetworkMapper linkNetworkMapper;
    private final LinkStatsTodayMapper linkStatsTodayMapper;
    private final GotoDomainWhiteListConfiguration gotoDomainWhiteListConfiguration;

    @Value("${short-link.stats.locale.amap-key}")
    private String statsLocaleAmapKey;

    @Value("${short-link.domain.default}")
    private String createShortLinkDefaultDomain;

    @SneakyThrows
    @Override
    public void restoreUrl(String shortUrl, HttpServletRequest request, HttpServletResponse response) {
        //获取短链接网站的域名
        String serverName = request.getServerName();
        //TODO 不确定是否要自己直接指定域名与端口
        String serverPort = Optional.of(request.getServerPort())
                .filter(each -> !Objects.equals(each, 80))
                .map(String::valueOf)
                .map(each -> ":" + each)
                .orElse("");
        String fullShortUrl = serverName + serverPort + "/" + shortUrl;
        //先查询缓存，判断缓存中是否存在
        String originalUrl = stringRedisTemplate.opsForValue().get(String.format(GOTO_SHORT_LINK_KEY, fullShortUrl));
        if (StrUtil.isNotBlank(originalUrl)) {
            shortLinkStats(fullShortUrl, null, request, response);
            response.sendRedirect(originalUrl);
            return;
        }

        boolean contains = shortLinkCreateCacheBloomFilter.contains(fullShortUrl);
        if (!contains) {
            response.sendRedirect("/page/notfound");
            return;
        }

        //第一，这个是考虑缓存击穿，若是数据库已经确定这个Key不存在，则会将这个Key缓存一个空值。如果不加以判断，又会将请求打到数据库，等于对这个Key缓存的空值没有派上用场。
        //第二，布隆过滤器对值存在的情况下仍然可能误判，所以需要再查一次，避免许多误判的请求打到数据库。

        String gotoIsNullShortLink = stringRedisTemplate.opsForValue().get(String.format(GOTO_IS_NULL_SHORT_LINK_KEY, fullShortUrl));
        if (StrUtil.isNotBlank(gotoIsNullShortLink)) {
            response.sendRedirect("/page/notfound");
            return;
        }

        RLock lock = redissonClient.getLock(String.format(LOCK_GOTO_SHORT_LINK_KEY, fullShortUrl));
        lock.lock();
        try {
            //里面再写个双重判定，防止并发线程进来时所有线程均获取锁去查询数据库
            originalUrl = stringRedisTemplate.opsForValue().get(String.format(GOTO_SHORT_LINK_KEY, fullShortUrl));
            if (StrUtil.isNotBlank(originalUrl)) {
                shortLinkStats(fullShortUrl, null, request, response);
                response.sendRedirect(originalUrl);
                return;
            }
            //1.先去goto表中查询当前fullShortUrl所在的gid
            LambdaQueryWrapper<ShortLinkGoto> linkGotoQueryWrapper = Wrappers.lambdaQuery(ShortLinkGoto.class)
                    .eq(ShortLinkGoto::getFullShortUrl, fullShortUrl);
            ShortLinkGoto shortLinkGoto = shortLinkGotoMapper.selectOne(linkGotoQueryWrapper);
            if (shortLinkGoto == null) {
                stringRedisTemplate.opsForValue().set(String.format(GOTO_IS_NULL_SHORT_LINK_KEY, fullShortUrl), "-", 30, TimeUnit.MINUTES);
                response.sendRedirect("/page/notfound");
                return;
            }
            LambdaQueryWrapper<ShortLink> queryWrapper = Wrappers.lambdaQuery(ShortLink.class)
                    .eq(ShortLink::getGid, shortLinkGoto.getGid())
                    .eq(ShortLink::getFullShortUrl, fullShortUrl)
                    .eq(ShortLink::getDelFlag, 0)
                    .eq(ShortLink::getEnableStatus, 0);
            ShortLink shortLink = baseMapper.selectOne(queryWrapper);
            if (shortLink == null || (shortLink.getValidDate() != null && shortLink.getValidDate().before(new Date()))) {
                stringRedisTemplate.opsForValue().set(String.format(GOTO_IS_NULL_SHORT_LINK_KEY, fullShortUrl), "-", 30, TimeUnit.MINUTES);
                response.sendRedirect("/page/notfound");
                return;
            }
            stringRedisTemplate.opsForValue().set(
                    String.format(GOTO_SHORT_LINK_KEY, fullShortUrl),
                    shortLink.getOriginUrl(),
                    LinkUtil.getShortLinkCacheValidDate(shortLink.getValidDate()),
                    TimeUnit.MILLISECONDS
            );
            shortLinkStats(fullShortUrl, shortLink.getGid(), request, response);
            response.sendRedirect(shortLink.getOriginUrl());
        } finally {
            lock.unlock();
        }
    }

    @Transactional
    @Override
    public ShortLinkCreateRespVO createShortLink(ShortLinkCreateReqDTO requestParam) {
        verificationWhitelist(requestParam.getOriginUrl());
        String shortLinkSuffix = generatedSuffix(requestParam);
        String fullShortUrl = StrBuilder.create(createShortLinkDefaultDomain)
                .append("/")
                .append(shortLinkSuffix)
                .toString();
        ShortLink shortLink = ShortLink.builder()
                .fullShortUrl(fullShortUrl)
                .shortUri(shortLinkSuffix)
                .createdType(requestParam.getCreatedType())
                .enableStatus(0)
                .favicon(getFavicon(requestParam.getOriginUrl()))
                .describe(requestParam.getDescribe())
                .originUrl(requestParam.getOriginUrl())
                .domain(createShortLinkDefaultDomain)
                .gid(requestParam.getGid())
                .totalPv(0)
                .totalUv(0)
                .totalUip(0)
                .validDateType(requestParam.getValidDateType())
                .validDate(requestParam.getValidDate())
                .build();
        ShortLinkGoto linkGoto = ShortLinkGoto.builder()
                .fullShortUrl(fullShortUrl)
                .gid(requestParam.getGid())
                .build();
        try {
            baseMapper.insert(shortLink);
            shortLinkGotoMapper.insert(linkGoto);
        } catch (DuplicateKeyException e) {
            log.warn("短链接:{} 重复", fullShortUrl);
            throw new ClientException("短链接重复创建");
        }
        stringRedisTemplate.opsForValue().set(
                String.format(GOTO_SHORT_LINK_KEY, fullShortUrl),
                requestParam.getOriginUrl(),
                LinkUtil.getShortLinkCacheValidDate(requestParam.getValidDate()),
                TimeUnit.MILLISECONDS
        );
        shortLinkCreateCacheBloomFilter.add(fullShortUrl);
        return ShortLinkCreateRespVO.builder()
                .fullShortUrl("http://" + shortLink.getFullShortUrl())
                .gid(requestParam.getGid())
                .originUrl(requestParam.getOriginUrl())
                .build();
    }

    @Transactional
    @Override
    public ShortLinkBatchCreateRespVO batchCreateShortLink(ShortLinkBatchCreateReqDTO requestParam) {
        List<String> originUrls = requestParam.getOriginUrls();
        List<String> describes = requestParam.getDescribes();
        if (ObjectUtil.isAllEmpty(originUrls)) {
            throw new ClientException(CLIENT_ERROR);
        }
        List<ShortLinkBatchBaseInfoRespVO> results = new ArrayList<>();

        for (int i = 0; i < originUrls.size(); i++) {
            ShortLinkCreateReqDTO shortLinkCreateReqDTO = BeanUtil.toBean(requestParam, ShortLinkCreateReqDTO.class);
            shortLinkCreateReqDTO.setOriginUrl(originUrls.get(i));
            shortLinkCreateReqDTO.setDescribe(describes.get(i));
            try {
                ShortLinkCreateRespVO shortLink = createShortLink(shortLinkCreateReqDTO);
                ShortLinkBatchBaseInfoRespVO linkBaseInfoRespDTO = ShortLinkBatchBaseInfoRespVO.builder()
                        .fullShortUrl(shortLink.getFullShortUrl())
                        .originUrl(shortLink.getOriginUrl())
                        .describe(describes.get(i))
                        .build();
                results.add(linkBaseInfoRespDTO);
            } catch (Exception e) {
                log.error("批量创建短链接失败，原始参数：{}", originUrls.get(i));
            }
        }
        return ShortLinkBatchCreateRespVO.builder()
                .total(results.size())
                .baseLinkInfos(results)
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateShortLink(ShortLinkUpdateReqDTO requestParam) {
        verificationWhitelist(requestParam.getOriginUrl());
        LambdaQueryWrapper<ShortLink> queryWrapper = Wrappers.lambdaQuery(ShortLink.class)
                .eq(ShortLink::getGid, requestParam.getOriginGid())
                .eq(ShortLink::getFullShortUrl, requestParam.getFullShortUrl())
                .eq(ShortLink::getDelFlag, 0)
                .eq(ShortLink::getEnableStatus, 0);
        ShortLink hasShortLink = baseMapper.selectOne(queryWrapper);
        if (hasShortLink == null) {
            throw new ClientException("修改的短链接不存在，可能已被删除");
        }
        if (Objects.equals(hasShortLink.getGid(), requestParam.getGid())) {
            LambdaUpdateWrapper<ShortLink> updateWrapper = Wrappers.lambdaUpdate(ShortLink.class)
                    .eq(ShortLink::getFullShortUrl, requestParam.getFullShortUrl())
                    .eq(ShortLink::getGid, requestParam.getGid())
                    .eq(ShortLink::getDelFlag, 0)
                    .eq(ShortLink::getEnableStatus, 0)
                    .set(Objects.equals(requestParam.getValidDateType(), 0), ShortLink::getValidDate, null);
            ShortLink shortLink = ShortLink.builder()
                    .domain(hasShortLink.getDomain())
                    .shortUri(hasShortLink.getShortUri())
                    .favicon(hasShortLink.getFavicon())
                    .createdType(hasShortLink.getCreatedType())
                    .totalPv(hasShortLink.getTotalPv())
                    .totalUv(hasShortLink.getTotalUv())
                    .todayUip(hasShortLink.getTotalUip())
                    .gid(requestParam.getGid())
                    .originUrl(requestParam.getOriginUrl())
                    .describe(requestParam.getDescribe())
                    .validDateType(requestParam.getValidDateType())
                    .validDate(requestParam.getValidDate())
                    .build();
            baseMapper.update(shortLink, updateWrapper);
        } else {
            LambdaUpdateWrapper<ShortLink> linkUpdateWrapper = Wrappers.lambdaUpdate(ShortLink.class)
                    .eq(ShortLink::getFullShortUrl, requestParam.getFullShortUrl())
                    .eq(ShortLink::getGid, hasShortLink.getGid())
                    .eq(ShortLink::getDelFlag, 0)
                    .eq(ShortLink::getEnableStatus, 0);
            ShortLink shortLink = new ShortLink();
            shortLink.setDelFlag(1);
            baseMapper.update(shortLink, linkUpdateWrapper);
            ShortLink shortLinkDO = ShortLink.builder()
                    .domain(hasShortLink.getDomain())
                    .originUrl(requestParam.getOriginUrl())
                    .gid(requestParam.getGid())
                    .favicon(hasShortLink.getFavicon())
                    .createdType(hasShortLink.getCreatedType())
                    .validDateType(requestParam.getValidDateType())
                    .validDate(requestParam.getValidDate())
                    .describe(requestParam.getDescribe())
                    .shortUri(hasShortLink.getShortUri())
                    .enableStatus(hasShortLink.getEnableStatus())
                    .fullShortUrl(hasShortLink.getFullShortUrl())
                    .totalPv(hasShortLink.getTotalPv())
                    .totalUv(hasShortLink.getTotalUv())
                    .todayUip(hasShortLink.getTotalUip())
                    .build();
            baseMapper.insert(shortLinkDO);
        }
        //判断是否修改了有效期，修改了就直接将缓存删除，让跳转时重建存，防止短链接过期后还是可以跳转
        if(!Objects.equals(hasShortLink.getValidDateType(),requestParam.getValidDateType())
                || !Objects.equals(hasShortLink.getValidDate(),requestParam.getValidDate())) {
            stringRedisTemplate.delete(String.format(GOTO_SHORT_LINK_KEY, requestParam.getFullShortUrl()));
            //先判断缓存中是否存在空页面的缓存
            if(hasShortLink.getValidDate() != null && hasShortLink.getValidDate().before(new Date())){
                if(Objects.equals(requestParam.getValidDateType(), VailDateTypeEnum.PERMANENT.getType()) || requestParam.getValidDate().after(new Date())){
                    stringRedisTemplate.delete(String.format(GOTO_IS_NULL_SHORT_LINK_KEY, requestParam.getFullShortUrl()));
                }
            }
        }
    }

    @Override
    public IPage<ShortLinkPageRespVO> pageShortLink(ShortLinkPageDTO requestParam) {
        IPage<ShortLink> resultPage = baseMapper.pageShortLink(requestParam);
        return resultPage.convert(each -> {
            ShortLinkPageRespVO result = BeanUtil.toBean(each, ShortLinkPageRespVO.class);
            result.setDomain("http://" + result.getDomain());
            return result;
        });
    }

    @Override
    public List<ShortLinkCountRespVO> listCountShortLink(List<String> requestParam) {
        //select gid,count(*) from t_link in (gid) group by gid order by create_time desc;
        QueryWrapper<ShortLink> queryWrapper = Wrappers.query(new ShortLink())
                .select("gid as gid ,count(*) as shortLinkCount")
                .in("gid", requestParam)
                .eq("enable_status", 0)
                .eq("del_flag", 0)
                .groupBy("gid");
        List<Map<String, Object>> shortLinkList = baseMapper.selectMaps(queryWrapper);
        return BeanUtil.copyToList(shortLinkList, ShortLinkCountRespVO.class);
    }

    private void shortLinkStats(String fullShortUrl, String gid, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        AtomicBoolean uvFirstFlag = new AtomicBoolean();
        AtomicReference<String> uv = new AtomicReference<>();
        Date date = new Date();
        Runnable addResponseCookieTask = () -> {
            uv.set(UUID.randomUUID().toString());
            Cookie uvCookie = new Cookie("uv", uv.get());
            uvCookie.setPath(StrUtil.sub(fullShortUrl, fullShortUrl.indexOf("/"), fullShortUrl.length()));
            //设置cookie有效期
            uvCookie.setMaxAge(60 * 60 * 24 * 30);
            uvFirstFlag.set(Boolean.TRUE);
            stringRedisTemplate.opsForSet().add("short-link:stats:uv:" + fullShortUrl, uv.get());
            //给uv与uip增加个1天的过期时间，方便增加每日的访问统计的uv与pv
            stringRedisTemplate.expire("short-link:stats:uv:" + fullShortUrl, 1, TimeUnit.DAYS);
            response.addCookie(uvCookie);
        };

        if (ArrayUtil.isNotEmpty(cookies)) {
            Arrays.stream(cookies)
                    .filter(each -> Objects.equals(each.getName(), "uv"))
                    .findFirst()
                    .map(Cookie::getValue)
                    .ifPresentOrElse(each -> {
                        uv.set(each);
                        //因为set集合是无重复数值的,redis中存储的是独一无二的uuid
                        //问题cookie是30天过期，所以还需要将缓存中的key过期时间也设置成30天不然会一直存储导致缓存不足
                        //TODO 大量的cookie，怎么保证redis不被其给撑爆
                        Long uvAdded = stringRedisTemplate.opsForSet().add("short-link:stats:uv:" + fullShortUrl, each);
                        stringRedisTemplate.expire("short-link:stats:uv:" + fullShortUrl, 1, TimeUnit.DAYS);
                        uvFirstFlag.set(uvAdded != null && uvAdded > 0L);
                    }, addResponseCookieTask);
        } else {
            addResponseCookieTask.run();
        }

        //TODO 大量的uip，怎么保证redis不被其给撑爆
        String actualIp = LinkUtil.getActualIp(request);
        Long uipAdded = stringRedisTemplate.opsForSet().add("short-link:stats:uip:" + fullShortUrl, actualIp);
        stringRedisTemplate.expire("short-link:stats:uip:" + fullShortUrl, 1, TimeUnit.DAYS);
        boolean uipFirstFlag = uipAdded != null && uipAdded > 0L;

        if (StrUtil.isBlank(gid)) {
            LambdaQueryWrapper<ShortLinkGoto> queryWrapper = Wrappers.lambdaQuery(ShortLinkGoto.class)
                    .eq(ShortLinkGoto::getFullShortUrl, fullShortUrl);
            ShortLinkGoto shortLinkGoto = shortLinkGotoMapper.selectOne(queryWrapper);
            gid = shortLinkGoto.getGid();
        }
        int hourOfDay = LocalTime.now().getHour();
        int dayOfWeek = LocalDate.now().getDayOfWeek().getValue();
        LinkAccessStats linkAccessStats = LinkAccessStats.builder()
                .pv(1)
                .uv(uvFirstFlag.get() ? 1 : 0)
                .uip(uipFirstFlag ? 1 : 0)
                .hour(hourOfDay)
                .weekday(dayOfWeek)
                .date(date)
                .fullShortUrl(fullShortUrl)
                .gid(gid)
                .build();
        linkAccessMapper.shortLinkStats(linkAccessStats);

        //保存到地区监控数据到数据库
        Map<String, Object> localeParamMap = new HashMap<>();
        localeParamMap.put("key", statsLocaleAmapKey);
        localeParamMap.put("ip", actualIp);
        //返回的是JSON格式的数据转成的字符串
        String localeResultStr = HttpUtil.get(AMAP_REMOTE_URL, localeParamMap);
        JSONObject localeResultObj = JSONUtil.parseObj(localeResultStr);
        String infoCode = localeResultObj.getStr("infocode");
        String actualProvince = localeResultObj.getStr("province");
        String actualCity = localeResultObj.getStr("city");
        String actualAdcode = localeResultObj.getStr("adcode");
        if (StrUtil.isNotBlank(infoCode) && StrUtil.equals(infoCode, "10000")) {
            LinkLocaleStats linkLocaleStats = LinkLocaleStats.builder()
                    .fullShortUrl(fullShortUrl)
                    .gid(gid)
                    .country("中国")
                    .province(StrUtil.equals(actualProvince, "[]") ? "未知" : actualProvince)
                    .city(StrUtil.equals(actualProvince, "[]") ? "未知" : actualCity)
                    .adcode(StrUtil.equals(actualAdcode, "[]") ? "未知" : actualAdcode)
                    .cnt(1)
                    .date(date)
                    .build();
            linkLocaleMapper.shortLinkLocaleState(linkLocaleStats);
        }

        //保存到操作系统监控数据到数据库
        String os = LinkUtil.getOs(request);
        LinkOsStats linkOsStats = LinkOsStats.builder()
                .os(os)
                .cnt(1)
                .fullShortUrl(fullShortUrl)
                .gid(gid)
                .date(date)
                .build();
        linkOsMapper.shortLinkOsState(linkOsStats);

        //保存到浏览器监控数据到数据库
        String browser = LinkUtil.getBrowser(request);
        LinkBrowserStats linkBrowserStats = LinkBrowserStats.builder()
                .browser(browser)
                .cnt(1)
                .fullShortUrl(fullShortUrl)
                .gid(gid)
                .date(date)
                .build();
        linkBrowserMapper.shortLinkBrowserStats(linkBrowserStats);

        //保存设备数据到数据库
        String device = LinkUtil.getDevice(request);
        LinkDeviceStats linkDeviceStats = LinkDeviceStats.builder()
                .device(device)
                .cnt(1)
                .gid(gid)
                .fullShortUrl(fullShortUrl)
                .date(date)
                .build();
        linkDeviceMapper.shortLinkDeviceState(linkDeviceStats);

        //保存网络数据到数据库
        String network = LinkUtil.getNetwork(request);
        LinkNetworkStats linkNetworkStats = LinkNetworkStats.builder()
                .network(network)
                .cnt(1)
                .gid(gid)
                .fullShortUrl(fullShortUrl)
                .date(date)
                .build();
        linkNetworkMapper.shortLinkNetworkState(linkNetworkStats);

        //保存短链接监控日志数据到数据库
        LinkAccessLogs linkAccessLogs = LinkAccessLogs.builder()
                .ip(actualIp)
                .user(uv.get())
                .fullShortUrl(fullShortUrl)
                .gid(gid)
                .browser(browser)
                .os(os)
                .network(network)
                .locale(StrUtil.join("-", "中国", actualProvince, actualCity))
                .device(device)
                .build();
        linkAccessLogsMapper.insert(linkAccessLogs);

        //增加总统计的访问数
        baseMapper.incrementStats(fullShortUrl, gid, 1, uvFirstFlag.get() ? 1 : 0, uipFirstFlag ? 1 : 0);

        //保存短链接今日统计数据到数据库
        LinkStatsToday linkStatsToday = LinkStatsToday.builder()
                .fullShortUrl(fullShortUrl)
                .gid(gid)
                .todayPv(1)
                .todayUv(uvFirstFlag.get() ? 1 : 0)
                .todayUip(uipFirstFlag ? 1 : 0)
                .date(date)
                .build();
        linkStatsTodayMapper.shortLinkTodayState(linkStatsToday);
    }

    private String generatedSuffix(ShortLinkCreateReqDTO requestParam) {
        int customGenerateCount = 0;
        String shortLinkUri;
        while (true) {
            if (customGenerateCount > 10) {
                throw new ClientException("创建短链接过于频繁，请稍后再试");
            }
            String originUrl = requestParam.getOriginUrl();
            originUrl += System.currentTimeMillis();
            shortLinkUri = HashUtil.hashToBase62(originUrl);
            if (!shortLinkCreateCacheBloomFilter.contains(createShortLinkDefaultDomain + "/" + shortLinkUri)) {
                break;
            }
            customGenerateCount++;
        }
        return shortLinkUri;
    }

    @SneakyThrows
    private String getFavicon(String url) {
        //TODO 存入OSS服务
        URL tagetUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) tagetUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            Document document = Jsoup.connect(url).get();
            Elements linkElements = document.select("link[rel~=(?i)^(shortcut )?icon]");
            if (!linkElements.isEmpty()) {
                Element linkElement = linkElements.first();
                return linkElement.absUrl("href");
            }
        }
        return null;
    }

    private void verificationWhitelist(String originUrl){
        Boolean enable = gotoDomainWhiteListConfiguration.getEnable();
        if(enable == null || !enable){
            return;
        }
        //去除origin中的www
        String domain = LinkUtil.extractDomain(originUrl);
        if (StrUtil.isBlank(domain)) {
            throw new ClientException("跳转链接填写错误");
        }
        List<String> whitelist = gotoDomainWhiteListConfiguration.getDetails();
        if(!whitelist.contains(domain)){
            throw new ClientException("跳转的网站异常，请生成以下网站跳转链接：" + gotoDomainWhiteListConfiguration.getDetails());
        }
    }
}
