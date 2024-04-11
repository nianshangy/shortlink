package com.nian.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nian.shortlink.project.domain.entity.ShortLink;
import com.nian.shortlink.project.domain.req.link.ShortLinkBatchCreateReqDTO;
import com.nian.shortlink.project.domain.req.link.ShortLinkCreateReqDTO;
import com.nian.shortlink.project.domain.req.link.ShortLinkPageDTO;
import com.nian.shortlink.project.domain.req.link.ShortLinkUpdateReqDTO;
import com.nian.shortlink.project.domain.resp.link.ShortLinkBatchCreateRespVO;
import com.nian.shortlink.project.domain.resp.link.ShortLinkCountRespVO;
import com.nian.shortlink.project.domain.resp.link.ShortLinkCreateRespVO;
import com.nian.shortlink.project.domain.resp.link.ShortLinkPageRespVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 短链接接口
 */
public interface IShortLinkService extends IService<ShortLink> {

    /**
     * 创建短链接
     * @param requestParam 短链接创建请求参数
     * @return 短链接创建响应结果
     */
    ShortLinkCreateRespVO createShortLink(ShortLinkCreateReqDTO requestParam);

    /**
     * 分页查寻短链接
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

    /**
     * 修改短链接
     * @param requestParam 修改短链接请求参数
     */
    void updateShortLink(ShortLinkUpdateReqDTO requestParam);

    /**
     * 短链接跳转
     * @param shortUrl 短链接
     * @param request Http 请求
     * @param response Http 响应
     */
    void restoreUrl(String shortUrl, HttpServletRequest request, HttpServletResponse response);

    /**
     * 批量创建短链接
     * @param requestParam 批量创建短链接请求参数
     * @return 短链接批量创建响应参数
     */
    ShortLinkBatchCreateRespVO batchCreateShortLink(ShortLinkBatchCreateReqDTO requestParam);
}
