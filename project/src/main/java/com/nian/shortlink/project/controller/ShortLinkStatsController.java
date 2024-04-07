package com.nian.shortlink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nian.shortlink.project.common.convention.result.Result;
import com.nian.shortlink.project.common.convention.result.ResultUtils;
import com.nian.shortlink.project.domain.req.ShortLinkAccessRecordReqDTO;
import com.nian.shortlink.project.domain.req.ShortLinkGroupAccessRecordReqDTO;
import com.nian.shortlink.project.domain.req.ShortLinkGroupStatsReqDTO;
import com.nian.shortlink.project.domain.req.ShortLinkStatsReqDTO;
import com.nian.shortlink.project.domain.resp.ShortLinkAccessRecordRespVO;
import com.nian.shortlink.project.domain.resp.ShortLinkStatsRespVO;
import com.nian.shortlink.project.service.IShortLinkStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短链接监控控制层
 */
@RestController
@RequiredArgsConstructor
public class ShortLinkStatsController {
    private final IShortLinkStatsService shortLinkStatsService;

    /**
     * 访问单个短链接指定时间内监控数据
     */
    @GetMapping("/api/short-link/v1/stats")
    public Result<ShortLinkStatsRespVO> shortLinkStats(ShortLinkStatsReqDTO requestParam){
        return ResultUtils.success(shortLinkStatsService.oneShortLinkStats(requestParam),"查询成功！");
    }

    /**
     * 访问分组短链接指定时间内监控数据
     */
    @GetMapping("/api/short-link/v1/stats/group")
    public Result<ShortLinkStatsRespVO> groupShortLinkStats(ShortLinkGroupStatsReqDTO requestParam){
        return ResultUtils.success(shortLinkStatsService.groupShortLinkStats(requestParam),"查询成功！");
    }

    /**
     * 访问单个短链接指定时间内的访问记录监控数据
     */
    @GetMapping("/api/short-link/v1/stats/access-record")
    public Result<IPage<ShortLinkAccessRecordRespVO>> shortLinkStatsAccessRecord(ShortLinkAccessRecordReqDTO requestParam){
        return ResultUtils.success(shortLinkStatsService.shortLinkStatsAccessRecord(requestParam),"查询成功！");
    }

    /**
     * 访问分组短链接指定时间内访问记录监控数据
     */
    @GetMapping("/api/short-link/v1/stats/access-record/group")
    public Result<IPage<ShortLinkAccessRecordRespVO>> groupShortLinkStatsAccessRecord(ShortLinkGroupAccessRecordReqDTO requestParam) {
        return ResultUtils.success(shortLinkStatsService.groupShortLinkStatsAccessRecord(requestParam),"查询成功！");
    }

}
