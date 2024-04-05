package com.nian.shortlink.project.domain.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 短链接监控响应参数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkStatsRespVO {
    /**
     * 访问量
     */
    private Integer pv;

    /**
     * 独立访客数
     */
    private Integer uv;

    /**
     * 独立IP数
     */
    private Integer uip;

    /**
     * 基础访问详情
     */
    private List<ShortLinkStatsAccessDailyRespVO> daily;

    /**
     * 地区访问详情（仅国内）
     */
    private List<ShortLinkStatsLocaleCNRespVO> localeCnStats;

    /**
     * 小时访问详情
     */
    private List<Integer> hourStats;

    /**
     * 高频访问IP详情
     */
    private List<ShortLinkStatsTopIpRespVO> topIpStats;

    /**
     * 一周访问详情
     */
    private List<Integer> weekdayStats;

    /**
     * 浏览器访问详情
     */
    private List<ShortLinkStatsBrowserRespVO> browserStats;

    /**
     * 操作系统访问详情
     */
    private List<ShortLinkStatsOsRespVO> osStats;

    /**
     * 访客访问类型详情
     */
    private List<ShortLinkStatsUvRespVO> uvTypeStats;

    /**
     * 访问设备类型详情
     */
    private List<ShortLinkStatsDeviceRespVO> deviceStats;

    /**
     * 访问网络类型详情
     */
    private List<ShortLinkStatsNetworkRespVO> networkStats;
}
