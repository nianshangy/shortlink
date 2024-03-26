package com.nian.shortlink.project.domain.req;

import lombok.Data;

/**
 * 保存到回收站请求参数
 */
@Data
public class RecycleBinSaveReqDTO {

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 完整短链接
     */
    private String fullShortUrl;
}
