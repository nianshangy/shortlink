package com.nian.shortlink.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nian.shortlink.admin.common.convention.result.Result;
import com.nian.shortlink.admin.remote.ShortLinkActualRemoteService;
import com.nian.shortlink.admin.remote.req.linkStats.ShortLinkAccessRecordReqDTO;
import com.nian.shortlink.admin.remote.req.linkStats.ShortLinkGroupAccessRecordReqDTO;
import com.nian.shortlink.admin.remote.req.linkStats.ShortLinkGroupStatsReqDTO;
import com.nian.shortlink.admin.remote.req.linkStats.ShortLinkStatsReqDTO;
import com.nian.shortlink.admin.remote.resp.linkStats.ShortLinkAccessRecordRespVO;
import com.nian.shortlink.admin.remote.resp.linkStats.ShortLinkStatsRespVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短链接监控控制层
 */
@RestController(value = "shortLinkStatsControllerByAdmin")
@RequiredArgsConstructor
public class ShortLinkStatsController {

    private final ShortLinkActualRemoteService shortLinkActualRemoteService;

    /**
     * 访问单个短链接指定时间内监控数据
     */
    @GetMapping("/api/short-link/admin/v1/stats")
    public Result<ShortLinkStatsRespVO> shortLinkStats(ShortLinkStatsReqDTO requestParam){
        return shortLinkActualRemoteService.oneShortLinkStats(requestParam);
    }

    /**
     * 访问分组短链接指定时间内监控数据
     */
    @GetMapping("/api/short-link/admin/v1/stats/group")
    public Result<ShortLinkStatsRespVO> groupShortLinkStats(ShortLinkGroupStatsReqDTO requestParam){
        return shortLinkActualRemoteService.groupShortLinkStats(requestParam);
    }

    /**
     * 访问单个短链接指定时间内的访问记录监控数据
     */
    @GetMapping("/api/short-link/admin/v1/stats/access-record")
    public Result<Page<ShortLinkAccessRecordRespVO>> shortLinkStatsAccessRecord(ShortLinkAccessRecordReqDTO requestParam){
        return shortLinkActualRemoteService.shortLinkStatsAccessRecord(requestParam);
    }

    /**
     * 访问分组短链接指定时间内访问记录监控数据
     */
    @GetMapping("/api/short-link/admin/v1/stats/access-record/group")
    public Result<Page<ShortLinkAccessRecordRespVO>> groupShortLinkStatsAccessRecord(ShortLinkGroupAccessRecordReqDTO requestParam) {
        return shortLinkActualRemoteService.groupShortLinkStatsAccessRecord(requestParam);
    }

}
