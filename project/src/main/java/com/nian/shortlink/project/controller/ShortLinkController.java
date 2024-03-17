package com.nian.shortlink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nian.shortlink.project.common.convention.result.Result;
import com.nian.shortlink.project.common.convention.result.ResultUtils;
import com.nian.shortlink.project.domain.req.ShortLinkCreateReqDTO;
import com.nian.shortlink.project.domain.req.ShortLinkPageDTO;
import com.nian.shortlink.project.domain.resp.ShortLinkCreateRespVO;
import com.nian.shortlink.project.domain.resp.ShortLinkPageRespVO;
import com.nian.shortlink.project.service.IShortLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 短链接控制层
 */
@RestController
@RequiredArgsConstructor
public class ShortLinkController {

    private final IShortLinkService shortLinkService;

    /**
     * 创建短链接
     */
    @PostMapping("/api/short-link/v1/create")
    public Result<ShortLinkCreateRespVO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam){
        return ResultUtils.success(shortLinkService.createShortLink(requestParam),"创建短链接成功！");
    }

    /**
     * 短链接分页查寻
     */
    @GetMapping("/api/short-link/v1/page")
    public Result<IPage<ShortLinkPageRespVO>> pageShortLink(ShortLinkPageDTO requestParam){
        return ResultUtils.success(shortLinkService.pageShortLink(requestParam),"查询成功！");
    }
}
