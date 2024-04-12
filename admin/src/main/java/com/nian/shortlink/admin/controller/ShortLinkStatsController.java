package com.nian.shortlink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nian.shortlink.admin.common.convention.result.Result;
import com.nian.shortlink.admin.remote.ShortLinkRemoteService;
import com.nian.shortlink.admin.remote.req.linkStats.ShortLinkAccessRecordReqDTO;
import com.nian.shortlink.admin.remote.req.linkStats.ShortLinkGroupAccessRecordReqDTO;
import com.nian.shortlink.admin.remote.req.linkStats.ShortLinkGroupStatsReqDTO;
import com.nian.shortlink.admin.remote.req.linkStats.ShortLinkStatsReqDTO;
import com.nian.shortlink.admin.remote.resp.linkStats.ShortLinkAccessRecordRespVO;
import com.nian.shortlink.admin.remote.resp.linkStats.ShortLinkStatsRespVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短链接监控控制层
 */
@RestController
public class ShortLinkStatsController {
    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService(){};

    /**
     * 访问单个短链接指定时间内监控数据
     */
    @GetMapping("/api/short-link/admin/v1/stats")
    public Result<ShortLinkStatsRespVO> shortLinkStats(ShortLinkStatsReqDTO requestParam){
        return shortLinkRemoteService.oneShortLinkStats(requestParam);
    }

    /**
     * 访问分组短链接指定时间内监控数据
     */
    @GetMapping("/api/short-link/admin/v1/stats/group")
    public Result<ShortLinkStatsRespVO> groupShortLinkStats(ShortLinkGroupStatsReqDTO requestParam){
        return shortLinkRemoteService.groupShortLinkStats(requestParam);
    }

    /**
     * 访问单个短链接指定时间内的访问记录监控数据
     */
    @GetMapping("/api/short-link/admin/v1/stats/access-record")
    public Result<IPage<ShortLinkAccessRecordRespVO>> shortLinkStatsAccessRecord(ShortLinkAccessRecordReqDTO requestParam){
        return shortLinkRemoteService.shortLinkStatsAccessRecord(requestParam);
    }

    /**
     * 访问分组短链接指定时间内访问记录监控数据
     */
    @GetMapping("/api/short-link/admin/v1/stats/access-record/group")
    public Result<IPage<ShortLinkAccessRecordRespVO>> groupShortLinkStatsAccessRecord(ShortLinkGroupAccessRecordReqDTO requestParam) {
        return shortLinkRemoteService.groupShortLinkStatsAccessRecord(requestParam);
    }

}
