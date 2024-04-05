package com.nian.shortlink.project.controller;

import com.nian.shortlink.project.common.convention.result.Result;
import com.nian.shortlink.project.common.convention.result.ResultUtils;
import com.nian.shortlink.project.domain.req.ShortLinkStatsReqDTO;
import com.nian.shortlink.project.domain.resp.ShortLinkStatsRespVO;
import com.nian.shortlink.project.service.IShortLinkStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public Result<ShortLinkStatsRespVO> shortLinkStats(@RequestBody ShortLinkStatsReqDTO requestParam){
        return ResultUtils.success(shortLinkStatsService.oneShortLinkStats(requestParam),"查询成功！");
    }
}
