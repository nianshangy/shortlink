package com.nian.shortlink.project.domain.resp.linkStats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短链接设备监控响应参数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkStatsDeviceRespVO {

    /**
     * 统计
     */
    private Integer cnt;

    /**
     * 设备
     */
    private String device;

    /**
     * 占比
     */
    private Double ratio;
}
