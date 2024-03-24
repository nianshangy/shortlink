package com.nian.shortlink.project.controller;

import com.nian.shortlink.project.common.convention.result.Result;
import com.nian.shortlink.project.common.convention.result.ResultUtils;
import com.nian.shortlink.project.service.IUrlTitleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 网站标题控制层
 */
@RestController
@RequiredArgsConstructor
public class UrlTitleController {

    private final IUrlTitleService urlTitleService;

    /**
     * 根据 url 获取网站标题
     */
    @GetMapping("/api/short-link/v1/title")
    public Result<String> getTitleByUrl(@RequestParam("url") String url){
        return ResultUtils.success(urlTitleService.getTitleByUrl(url),"获取标题成功");
    }
}
