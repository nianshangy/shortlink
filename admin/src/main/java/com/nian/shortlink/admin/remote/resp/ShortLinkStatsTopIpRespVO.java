package com.nian.shortlink.admin.remote.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短链接高频ip监控响应参数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkStatsTopIpRespVO {
    /**
     * 统计
     */
    private Integer cnt;

    /**
     * IP
     */
    private String ip;
}