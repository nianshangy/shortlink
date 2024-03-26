package com.nian.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nian.shortlink.project.common.convention.exception.ClientException;
import com.nian.shortlink.project.domain.entity.ShortLink;
import com.nian.shortlink.project.domain.req.RecycleBinSaveReqDTO;
import com.nian.shortlink.project.domain.req.ShortLinkRecycleBinPageReqDTO;
import com.nian.shortlink.project.domain.resp.ShortLinkRecycleBinPageRespVO;
import com.nian.shortlink.project.mapper.ShortLinkMapper;
import com.nian.shortlink.project.service.IRecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import static com.nian.shortlink.project.common.constat.RedisKeyConstant.GOTO_SHORT_LINK_KEY;

/**
 * 回收站接口实现层
 */
@Service
@RequiredArgsConstructor
public class RecycleBinServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLink> implements IRecycleBinService {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void saveRecycleBin(RecycleBinSaveReqDTO requestParam) {
        //1.根据gid以及完整短链接查询数据库
        LambdaQueryWrapper<ShortLink> queryWrapper = Wrappers.lambdaQuery(ShortLink.class)
                .eq(ShortLink::getGid, requestParam.getGid())
                .eq(ShortLink::getFullShortUrl, requestParam.getFullShortUrl())
                .eq(ShortLink::getDelFlag, 0)
                .eq(ShortLink::getEnableStatus, 0);
        ShortLink shortLink = baseMapper.selectOne(queryWrapper);
        if (shortLink == null) {
            throw new ClientException("短链接不存在!");
        }
        //2.如果存在则更新短链接的enableStatus
        LambdaUpdateWrapper<ShortLink> updateWrapper = Wrappers.lambdaUpdate(ShortLink.class)
                .eq(ShortLink::getGid, requestParam.getGid())
                .eq(ShortLink::getFullShortUrl, requestParam.getFullShortUrl())
                .eq(ShortLink::getDelFlag, 0)
                .eq(ShortLink::getEnableStatus, 0);
        ShortLink updateShortLink = ShortLink.builder()
                .enableStatus(1)
                .build();
        baseMapper.update(updateShortLink,updateWrapper);
        //3.再从缓存中将短链接删除
        stringRedisTemplate.delete(String.format(GOTO_SHORT_LINK_KEY, requestParam.getFullShortUrl()));
    }

    @Override
    public IPage<ShortLinkRecycleBinPageRespVO> pageRecycleShortLink(ShortLinkRecycleBinPageReqDTO requestParam) {
        LambdaQueryWrapper<ShortLink> queryWrapper = Wrappers.lambdaQuery(ShortLink.class)
                .in(ShortLink::getGid, requestParam.getGidList())
                .eq(ShortLink::getEnableStatus, 1)
                .eq(ShortLink::getDelFlag, 0)
                .orderByDesc(ShortLink::getCreateTime);
        IPage<ShortLink> resultPage = baseMapper.selectPage(requestParam, queryWrapper);
        return resultPage.convert(each -> {
            ShortLinkRecycleBinPageRespVO result = BeanUtil.toBean(each, ShortLinkRecycleBinPageRespVO.class);
            result.setDomain("http://" + result.getDomain());
            return result;
        });
    }
}
