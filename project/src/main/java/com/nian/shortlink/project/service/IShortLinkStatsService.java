package com.nian.shortlink.project.service;

import com.nian.shortlink.project.domain.req.ShortLinkStatsReqDTO;
import com.nian.shortlink.project.domain.resp.ShortLinkStatsRespVO;

/**
 * 短链接监控接口层
 */
public interface IShortLinkStatsService{

    /**
     * 获取单个短链接监控数据
     *
     * @param requestParam 获取短链接监控数据请求参数
     * @return 短链接监控数据
     */
    ShortLinkStatsRespVO oneShortLinkStats(ShortLinkStatsReqDTO requestParam);
}
