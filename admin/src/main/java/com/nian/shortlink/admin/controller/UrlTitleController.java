package com.nian.shortlink.admin.controller;

import com.nian.shortlink.admin.common.convention.result.Result;
import com.nian.shortlink.admin.remote.ShortLinkRemoteService;
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

    ShortLinkRemoteService shortLinkRemoteService= new ShortLinkRemoteService(){};

    /**
     * 根据 url 获取网站标题
     */
    @GetMapping("/api/short-link/admin/v1/title")
    public Result<String> getTitleByUrl(@RequestParam("url") String url){
        return shortLinkRemoteService.getTitleByUrl(url);
    }
}
