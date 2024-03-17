package com.nian.shortlink.project.controller;

import com.nian.shortlink.project.common.convention.result.Result;
import com.nian.shortlink.project.common.convention.result.ResultUtils;
import com.nian.shortlink.project.domain.dto.req.ShortLinkCreateReqDTO;
import com.nian.shortlink.project.domain.dto.resp.ShortLinkCreateRespDTO;
import com.nian.shortlink.project.service.IShortLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短链接控制层
 */
@RestController
@RequiredArgsConstructor
public class ShortLinkController {

    private final IShortLinkService shortLinkServicew;

    /**
     * 短链接创建
     */
    @PostMapping("/api/short-link/v1/create")
    public Result<ShortLinkCreateRespDTO> ShortLinkCreate(@RequestBody ShortLinkCreateReqDTO requestParam){
        return ResultUtils.success(shortLinkServicew.shortLinkCreate(requestParam),"创建短链接成功！");
    }


}
