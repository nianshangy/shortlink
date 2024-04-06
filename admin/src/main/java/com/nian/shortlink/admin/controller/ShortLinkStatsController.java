package com.nian.shortlink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nian.shortlink.admin.common.convention.result.Result;
import com.nian.shortlink.admin.remote.ShortLinkRemoteService;
import com.nian.shortlink.admin.remote.req.ShortLinkAccessRecordReqDTO;
import com.nian.shortlink.admin.remote.req.ShortLinkStatsReqDTO;
import com.nian.shortlink.admin.remote.resp.ShortLinkAccessRecordRespVO;
import com.nian.shortlink.admin.remote.resp.ShortLinkStatsRespVO;
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
     * 访问单个短链接指定时间内的访问记录监控数据
     */
    @GetMapping("/api/short-link/admin/v1/stats/access-record")
    public Result<IPage<ShortLinkAccessRecordRespVO>> shortLinkStatsAccessRecord(ShortLinkAccessRecordReqDTO requestParam){
        return shortLinkRemoteService.shortLinkStatsAccessRecord(requestParam);
    }

}
