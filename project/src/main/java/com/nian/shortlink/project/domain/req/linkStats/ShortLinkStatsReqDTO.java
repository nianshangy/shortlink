package com.nian.shortlink.project.domain.req.linkStats;

import lombok.Data;

/**
 * 短链接监控统计请求参数
 */
@Data
public class ShortLinkStatsReqDTO {
    /**
     * 完整短链接
     */
    private String fullShortUrl;

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 开始日期
     */
    private String startDate;

    /**
     * 结束日期
     */
    private String endDate;
}
