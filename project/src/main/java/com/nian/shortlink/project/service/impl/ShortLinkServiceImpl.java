package com.nian.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.StrBuilder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nian.shortlink.project.common.convention.exception.ClientException;
import com.nian.shortlink.project.domain.entity.ShortLink;
import com.nian.shortlink.project.domain.req.ShortLinkCreateReqDTO;
import com.nian.shortlink.project.domain.req.ShortLinkPageDTO;
import com.nian.shortlink.project.domain.resp.ShortLinkCountRespVO;
import com.nian.shortlink.project.domain.resp.ShortLinkCreateRespVO;
import com.nian.shortlink.project.domain.resp.ShortLinkPageRespVO;
import com.nian.shortlink.project.mapper.ShortLinkMapper;
import com.nian.shortlink.project.service.IShortLinkService;
import com.nian.shortlink.project.utils.HashUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 短链接接口实现层
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLink> implements IShortLinkService {

    private final RBloomFilter<String> shortLinkCreateCacheBloomFilter;
    @Override
    public ShortLinkCreateRespVO createShortLink(ShortLinkCreateReqDTO requestParam) {
        String shortLinkSuffix = generatedSuffix(requestParam);
        String fullShortUrl = StrBuilder.create(requestParam.getDomain())
                .append("/")
                .append(shortLinkSuffix)
                .toString();
        ShortLink shortLink = ShortLink.builder()
                .fullShortUrl(fullShortUrl)
                .shortUri(shortLinkSuffix)
                .createdType(requestParam.getCreatedType())
                .enableStatus(0)
                .favicon(requestParam.getFavicon())
                .describe(requestParam.getDescribe())
                .originUrl(requestParam.getOriginUrl())
                .domain(requestParam.getDomain())
                .gid(requestParam.getGid())
                .validDateType(requestParam.getValidDateType())
                .validDate(requestParam.getValidDate())
                .build();
        try {
            baseMapper.insert(shortLink);
        } catch (DuplicateKeyException e) {
            log.warn("短链接:{} 重复",fullShortUrl);
            throw new ClientException("短链接重复创建");
        }
        shortLinkCreateCacheBloomFilter.add(fullShortUrl);
        return ShortLinkCreateRespVO.builder()
                .fullShortUrl(shortLink.getFullShortUrl())
                .gid(requestParam.getGid())
                .originUrl(requestParam.getOriginUrl())
                .build();
    }

    @Override
    public IPage<ShortLinkPageRespVO> pageShortLink(ShortLinkPageDTO requestParam) {
        LambdaQueryWrapper<ShortLink> queryWrapper = Wrappers.lambdaQuery(ShortLink.class)
                .eq(ShortLink::getGid, requestParam.getGid())
                .eq(ShortLink::getEnableStatus, 0)
                .eq(ShortLink::getDel_flag, 0)
                .orderByDesc(ShortLink::getCreate_time);
        IPage<ShortLink> resultPage = baseMapper.selectPage(requestParam, queryWrapper);
        return resultPage.convert(each -> BeanUtil.toBean(each, ShortLinkPageRespVO.class));
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

    private String generatedSuffix(ShortLinkCreateReqDTO requestParam){
        int customGenerateCount = 0;
        String shortLinkUri;
        while (true){
            if(customGenerateCount > 10){
                throw new ClientException("创建短链接过于频繁，请稍后再试");
            }
            String originUrl = requestParam.getOriginUrl();
            originUrl += System.currentTimeMillis();
            shortLinkUri = HashUtil.hashToBase62(originUrl);
            if(!shortLinkCreateCacheBloomFilter.contains(requestParam.getDomain() + "/" + shortLinkUri)){
                break;
            }
            customGenerateCount++;
        }
        return shortLinkUri;
    }
}
