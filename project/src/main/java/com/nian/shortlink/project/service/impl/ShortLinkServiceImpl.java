package com.nian.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.StrBuilder;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nian.shortlink.project.common.convention.exception.ClientException;
import com.nian.shortlink.project.domain.dto.req.ShortLinkCreateReqDTO;
import com.nian.shortlink.project.domain.dto.resp.ShortLinkCreateRespDTO;
import com.nian.shortlink.project.domain.entity.ShortLink;
import com.nian.shortlink.project.mapper.ShortLinkMapper;
import com.nian.shortlink.project.service.IShortLinkService;
import com.nian.shortlink.project.utils.HashUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.sharding.exception.metadata.DuplicatedIndexException;
import org.redisson.api.RBloomFilter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/**
 * 短链接接口实现层
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLink> implements IShortLinkService {

    private final RBloomFilter<String> shortLinkCreateCacheBloomFilter;
    @Override
    public ShortLinkCreateRespDTO shortLinkCreate(ShortLinkCreateReqDTO requestParam) {
        String shortLinkSuffix = generatedSuffix(requestParam);
        String fullShortUrl = StrBuilder.create(requestParam.getDomain())
                .append("/")
                .append(shortLinkSuffix)
                .toString();
        ShortLink shortLink = ShortLink.builder()
                .fullShortUrl(fullShortUrl)
                .shortUri(shortLinkSuffix)
                .createdType(requestParam.getCreatedType())
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
        return ShortLinkCreateRespDTO.builder()
                .fullShortUrl(shortLink.getFullShortUrl())
                .gid(requestParam.getGid())
                .originUrl(requestParam.getOriginUrl())
                .build();
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
