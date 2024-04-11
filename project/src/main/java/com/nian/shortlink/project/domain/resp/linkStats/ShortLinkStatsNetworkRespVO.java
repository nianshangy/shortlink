package com.nian.shortlink.project.domain.resp.linkStats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短链接网络监控响应参数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkStatsNetworkRespVO {

    /**
     * 统计
     */
    private Integer cnt;

    /**
     * 网络
     */
    private String network;

    /**
     * 占比
     */
    private Double ratio;
}
