package com.nian.shortlink.admin.remote.req.recycle;

import lombok.Data;

/**
 * 回收站恢复功能
 */
@Data
public class RecycleBinRecoverReqDTO {
    /**
     * 分组标识
     */
    private String gid;

    /**
     * 全部短链接
     */
    private String fullShortUrl;
}
