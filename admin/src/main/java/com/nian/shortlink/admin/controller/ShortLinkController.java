package com.nian.shortlink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nian.shortlink.admin.common.convention.result.Result;
import com.nian.shortlink.admin.remote.ShortLinkRemoteService;
import com.nian.shortlink.admin.remote.req.ShortLinkCreateReqDTO;
import com.nian.shortlink.admin.remote.req.ShortLinkPageDTO;
import com.nian.shortlink.admin.remote.resp.ShortLinkCreateRespVO;
import com.nian.shortlink.admin.remote.resp.ShortLinkPageRespVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短链接后管控制层
 */
@RestController
public class ShortLinkController {

    /**
     * TODO 后序将其换成Spring Cloud 的 feign 调用
     */
    ShortLinkRemoteService shortLinkRemoteService= new ShortLinkRemoteService(){};

    /**
     * 创建短链接
     */
    @PostMapping("/api/short-link/admin/v1/create")
    public Result<ShortLinkCreateRespVO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam){
        return shortLinkRemoteService.createShortLink(requestParam);
    }

    /**
     * 短链接分页查寻
     */
    @GetMapping("/api/short-link/admin/v1/page")
    public Result<IPage<ShortLinkPageRespVO>> pageShortLink(ShortLinkPageDTO requestParam){
        return shortLinkRemoteService.pageShortLink(requestParam);
    }

}
