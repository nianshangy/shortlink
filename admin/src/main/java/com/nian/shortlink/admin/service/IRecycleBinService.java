package com.nian.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nian.shortlink.admin.common.convention.result.Result;
import com.nian.shortlink.admin.remote.req.recycle.ShortLinkRecycleBinPageReqDTO;
import com.nian.shortlink.admin.remote.resp.recycle.ShortLinkRecycleBinPageRespVO;

/**
 * URL 回收站接口层
 * 公众号：马丁玩编程，回复：加群，添加马哥微信（备注：link）获取项目资料
 */
public interface IRecycleBinService {

    /**
     * 分页查询回收站短链接
     *
     * @param requestParam 请求参数
     * @return 返回参数包装
     */
    Result<Page<ShortLinkRecycleBinPageRespVO>> pageRecycleShortLink(ShortLinkRecycleBinPageReqDTO requestParam);
}
