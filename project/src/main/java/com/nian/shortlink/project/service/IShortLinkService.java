package com.nian.shortlink.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nian.shortlink.project.domain.dto.req.ShortLinkCreateReqDTO;
import com.nian.shortlink.project.domain.dto.resp.ShortLinkCreateRespDTO;
import com.nian.shortlink.project.domain.entity.ShortLink;

/**
 * 短链接接口
 */
public interface IShortLinkService extends IService<ShortLink> {

    /**
     * 短链接创建
     * @param requestParam 短链接创建请求参数
     * @return 短链接创建响应参数
     */
    ShortLinkCreateRespDTO shortLinkCreate(ShortLinkCreateReqDTO requestParam);
}
