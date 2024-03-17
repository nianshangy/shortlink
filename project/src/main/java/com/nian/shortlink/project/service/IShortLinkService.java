package com.nian.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nian.shortlink.project.domain.req.ShortLinkCreateReqDTO;
import com.nian.shortlink.project.domain.req.ShortLinkPageDTO;
import com.nian.shortlink.project.domain.resp.ShortLinkCreateRespVO;
import com.nian.shortlink.project.domain.entity.ShortLink;
import com.nian.shortlink.project.domain.resp.ShortLinkPageRespVO;

/**
 * 短链接接口
 */
public interface IShortLinkService extends IService<ShortLink> {

    /**
     * 短链接创建
     * @param requestParam 短链接创建请求参数
     * @return 短链接创建响应参数
     */
    ShortLinkCreateRespVO createShortLink(ShortLinkCreateReqDTO requestParam);

    /**
     * 短链接分页查寻
     * @param requestParam 短链接分页查寻请求参数
     * @return 短链接分页查寻响应参数
     */
    IPage<ShortLinkPageRespVO> pageShortLink(ShortLinkPageDTO requestParam);
}
