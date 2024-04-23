package com.nian.shortlink.project.mq.consumer;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nian.shortlink.project.common.convention.exception.ServiceException;
import com.nian.shortlink.project.domain.entity.*;
import com.nian.shortlink.project.mapper.*;
import com.nian.shortlink.project.mq.idempotent.MessageQueueIdempotentHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static com.nian.shortlink.project.common.constat.RedisKeyConstant.SHORT_LINK_STATS_LOCK_KEY;
import static com.nian.shortlink.project.common.constat.ShortLinkConstant.AMAP_REMOTE_URL;

/**
 * 短链接监控统计消息消费者
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ShortLinkStatsSaveConsumer implements StreamListener<String , MapRecord<String, String, String>> {

    private final ShortLinkGotoMapper shortLinkGotoMapper;
    private final LinkAccessMapper linkAccessMapper;
    private final LinkLocaleMapper linkLocaleMapper;
    private final LinkOsMapper linkOsMapper;
    private final LinkBrowserMapper linkBrowserMapper;
    private final LinkAccessLogsMapper linkAccessLogsMapper;
    private final LinkDeviceMapper linkDeviceMapper;
    private final LinkNetworkMapper linkNetworkMapper;
    private final LinkStatsTodayMapper linkStatsTodayMapper;
    private final ShortLinkMapper shortLinkMapper;
    private final RedissonClient redissonClient;
    private final StringRedisTemplate stringRedisTemplate;
    private final MessageQueueIdempotentHandler messageQueueIdempotentHandler;

    @Value("${short-link.stats.locale.amap-key}")
    private String statsLocaleAmapKey;

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        String stream = message.getStream();
        RecordId id = message.getId();
        if(! messageQueueIdempotentHandler.isMessageProcessed(id.toString())){
            //判断消息流程是否执行完成
            if(messageQueueIdempotentHandler.isAccomplish(id.toString())){
                return;
            }
            throw new ServiceException("消息未完成流程，需要消息队列重试");
        }
        try {
            Map<String, String> producerMap = message.getValue();
            String fullShortUrl = producerMap.get("fullShortUrl");
            if(StrUtil.isNotBlank(fullShortUrl)){
                String gid = producerMap.get("gid");
                ShortLinkStatsRecord shortLinkStatsRecord = JSON.parseObject(producerMap.get("statsRecord"), ShortLinkStatsRecord.class);
                shortLinkStats(fullShortUrl,gid,shortLinkStatsRecord);
            }
            stringRedisTemplate.opsForStream().delete(Objects.requireNonNull(stream),id.getValue());
        } catch (Throwable ex){
            // 某某某情况宕机了
            messageQueueIdempotentHandler.deleteMessageProcessed(id.toString());
            log.error("记录短链接监控消费异常", ex);
            throw ex;
        }
        messageQueueIdempotentHandler.setAccomplish(id.toString());
    }


    private void shortLinkStats(String fullShortUrl, String gid, ShortLinkStatsRecord shortLinkStatsRecord){
        fullShortUrl = Optional.ofNullable(fullShortUrl).orElse(shortLinkStatsRecord.getFullShortUrl());
        RLock lock = redissonClient.getLock(SHORT_LINK_STATS_LOCK_KEY);
        lock.lock();
        try {
            Date date = new Date();
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
                    .uv(shortLinkStatsRecord.getUvFirstFlag() ? 1 : 0)
                    .uip(shortLinkStatsRecord.getUipFirstFlag() ? 1 : 0)
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
            localeParamMap.put("ip", shortLinkStatsRecord.getActualIp());
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
                        .city(StrUtil.equals(actualCity, "[]") ? "未知" : actualCity)
                        .adcode(StrUtil.equals(actualAdcode, "[]") ? "未知" : actualAdcode)
                        .cnt(1)
                        .date(date)
                        .build();
                linkLocaleMapper.shortLinkLocaleState(linkLocaleStats);
            }

            //保存到操作系统监控数据到数据库
            LinkOsStats linkOsStats = LinkOsStats.builder()
                    .os(shortLinkStatsRecord.getOs())
                    .cnt(1)
                    .fullShortUrl(fullShortUrl)
                    .gid(gid)
                    .date(date)
                    .build();
            linkOsMapper.shortLinkOsState(linkOsStats);

            //保存到浏览器监控数据到数据库
            LinkBrowserStats linkBrowserStats = LinkBrowserStats.builder()
                    .browser(shortLinkStatsRecord.getBrowser())
                    .cnt(1)
                    .fullShortUrl(fullShortUrl)
                    .gid(gid)
                    .date(date)
                    .build();
            linkBrowserMapper.shortLinkBrowserStats(linkBrowserStats);

            //保存设备数据到数据库
            LinkDeviceStats linkDeviceStats = LinkDeviceStats.builder()
                    .device(shortLinkStatsRecord.getDevice())
                    .cnt(1)
                    .gid(gid)
                    .fullShortUrl(fullShortUrl)
                    .date(date)
                    .build();
            linkDeviceMapper.shortLinkDeviceState(linkDeviceStats);

            //保存网络数据到数据库
            LinkNetworkStats linkNetworkStats = LinkNetworkStats.builder()
                    .network(shortLinkStatsRecord.getNetwork())
                    .cnt(1)
                    .gid(gid)
                    .fullShortUrl(fullShortUrl)
                    .date(date)
                    .build();
            linkNetworkMapper.shortLinkNetworkState(linkNetworkStats);

            //保存短链接监控日志数据到数据库
            LinkAccessLogs linkAccessLogs = LinkAccessLogs.builder()
                    .ip(shortLinkStatsRecord.getActualIp())
                    .user(shortLinkStatsRecord.getUv())
                    .fullShortUrl(fullShortUrl)
                    .gid(gid)
                    .browser(shortLinkStatsRecord.getBrowser())
                    .os(shortLinkStatsRecord.getOs())
                    .network(shortLinkStatsRecord.getNetwork())
                    .locale(StrUtil.join("-", "中国",
                            StrUtil.equals(actualProvince, "[]") ? "未知" : actualProvince,
                            StrUtil.equals(actualCity, "[]") ? "未知" : actualCity))
                    .device(shortLinkStatsRecord.getDevice())
                    .build();
            linkAccessLogsMapper.insert(linkAccessLogs);

            //增加总统计的访问数
            shortLinkMapper.incrementStats(fullShortUrl, gid, 1, shortLinkStatsRecord.getUvFirstFlag() ? 1 : 0, shortLinkStatsRecord.getUipFirstFlag() ? 1 : 0);

            //保存短链接今日统计数据到数据库
            LinkStatsToday linkStatsToday = LinkStatsToday.builder()
                    .fullShortUrl(fullShortUrl)
                    .gid(gid)
                    .todayPv(1)
                    .todayUv(shortLinkStatsRecord.getUvFirstFlag() ? 1 : 0)
                    .todayUip(shortLinkStatsRecord.getUipFirstFlag() ? 1 : 0)
                    .date(date)
                    .build();
            linkStatsTodayMapper.shortLinkTodayState(linkStatsToday);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
