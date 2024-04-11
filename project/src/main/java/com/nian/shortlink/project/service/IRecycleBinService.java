package com.nian.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nian.shortlink.project.domain.entity.ShortLink;
import com.nian.shortlink.project.domain.req.recycle.RecycleBinRecoverReqDTO;
import com.nian.shortlink.project.domain.req.recycle.RecycleBinRemoveReqDTO;
import com.nian.shortlink.project.domain.req.recycle.RecycleBinSaveReqDTO;
import com.nian.shortlink.project.domain.req.recycle.ShortLinkRecycleBinPageReqDTO;
import com.nian.shortlink.project.domain.resp.recycle.ShortLinkRecycleBinPageRespVO;

/**
 * 回收站接口层
 */
public interface IRecycleBinService extends IService<ShortLink> {

    /**
     * 保存到回收站
     *
     * @param requestParam 保存到回收站请求参数
     */
    void saveRecycleBin(RecycleBinSaveReqDTO requestParam);

    /**
     * 分页查寻回收站短链接
     *
     * @param requestParam 分页查寻回收站短链接请求参数
     * @return 分页查寻回收站短链接响应结果
     */
    IPage<ShortLinkRecycleBinPageRespVO> pageRecycleShortLink(ShortLinkRecycleBinPageReqDTO requestParam);

    /**
     * 从回收站恢复短链接
     *
     * @param requestParam 恢复短链接请求参数
     */
    void recoverRecycleBin(RecycleBinRecoverReqDTO requestParam);

    /**
     * 从回收站移除短链接
     *
     * @param requestParam 移除短链接请求参数
     */
    void removeRecycleBin(RecycleBinRemoveReqDTO requestParam);
}
