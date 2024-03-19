package com.nian.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nian.shortlink.project.domain.req.ShortLinkCreateReqDTO;
import com.nian.shortlink.project.domain.req.ShortLinkPageDTO;
import com.nian.shortlink.project.domain.resp.ShortLinkCountRespVO;
import com.nian.shortlink.project.domain.resp.ShortLinkCreateRespVO;
import com.nian.shortlink.project.domain.entity.ShortLink;
import com.nian.shortlink.project.domain.resp.ShortLinkPageRespVO;

import java.util.List;

/**
 * 短链接接口
 */
public interface IShortLinkService extends IService<ShortLink> {

    /**
     * 短链接创建
     * @param requestParam 短链接创建请求参数
     * @return 短链接创建响应结果
     */
    ShortLinkCreateRespVO createShortLink(ShortLinkCreateReqDTO requestParam);

    /**
     * 短链接分页查寻
     * @param requestParam 短链接分页查寻请求参数
     * @return 短链接分页查寻响应结果
     */
    IPage<ShortLinkPageRespVO> pageShortLink(ShortLinkPageDTO requestParam);

    /**
     * 查询短链接分组内数量
     * @param requestParam 查询短链接分组内数量请求参数
     * @return 查询短链接分组内数量响应结果
     */
    List<ShortLinkCountRespVO> listCountShortLink(List<String> requestParam);
}
