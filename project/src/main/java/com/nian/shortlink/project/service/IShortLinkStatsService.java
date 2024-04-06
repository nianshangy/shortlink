package com.nian.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nian.shortlink.project.domain.req.ShortLinkAccessRecordReqDTO;
import com.nian.shortlink.project.domain.req.ShortLinkStatsReqDTO;
import com.nian.shortlink.project.domain.resp.ShortLinkAccessRecordRespVO;
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

    /**
     * 访问单个短链接指定时间内的访问记录监控数据
     *
     * @param requestParam 访问单个短链接的访问记录监控数据请求参数
     * @return 访问记录监控数据
     */
    IPage<ShortLinkAccessRecordRespVO> shortLinkStatsAccessRecord(ShortLinkAccessRecordReqDTO requestParam);
}
