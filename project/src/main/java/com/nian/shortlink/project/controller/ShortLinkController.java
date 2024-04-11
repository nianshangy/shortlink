package com.nian.shortlink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nian.shortlink.project.common.convention.result.Result;
import com.nian.shortlink.project.common.convention.result.ResultUtils;
import com.nian.shortlink.project.domain.req.link.ShortLinkBatchCreateReqDTO;
import com.nian.shortlink.project.domain.req.link.ShortLinkCreateReqDTO;
import com.nian.shortlink.project.domain.req.link.ShortLinkPageDTO;
import com.nian.shortlink.project.domain.req.link.ShortLinkUpdateReqDTO;
import com.nian.shortlink.project.domain.resp.link.ShortLinkBatchCreateRespVO;
import com.nian.shortlink.project.domain.resp.link.ShortLinkCountRespVO;
import com.nian.shortlink.project.domain.resp.link.ShortLinkCreateRespVO;
import com.nian.shortlink.project.domain.resp.link.ShortLinkPageRespVO;
import com.nian.shortlink.project.service.IShortLinkService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 短链接控制层
 */
@RestController
@RequiredArgsConstructor
public class ShortLinkController {

    private final IShortLinkService shortLinkService;


    /**
     * 短链接跳转
     */
    @GetMapping("/{short-url}")
    public void restoreUrl(@PathVariable("short-url")String shortUrl, HttpServletRequest request, HttpServletResponse response){
        shortLinkService.restoreUrl(shortUrl,request,response);
    }

    /**
     * 创建短链接
     */
    @PostMapping("/api/short-link/v1/create")
    public Result<ShortLinkCreateRespVO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam){
        return ResultUtils.success(shortLinkService.createShortLink(requestParam),"创建短链接成功！");
    }


    /**
     * 批量创建短链接
     */
    @PostMapping("/api/short-link/v1/create/batch")
    public Result<ShortLinkBatchCreateRespVO> batchCreateShortLink(@RequestBody ShortLinkBatchCreateReqDTO requestParam) {
        return ResultUtils.success(shortLinkService.batchCreateShortLink(requestParam));
    }

    /**
     * 修改短链接
     */
    @PostMapping ("/api/short-link/v1/update")
    public Result<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam){
        shortLinkService.updateShortLink(requestParam);
        return ResultUtils.success("修改短链接成功！");
    }

    /**
     * 短链接分页查寻
     */
    @GetMapping("/api/short-link/v1/page")
    public Result<IPage<ShortLinkPageRespVO>> pageShortLink(ShortLinkPageDTO requestParam){
        return ResultUtils.success(shortLinkService.pageShortLink(requestParam),"查询成功！");
    }

    /**
     * 查询短链接分组内数量
     */
    @GetMapping("/api/short-link/v1/count")
    public Result<List<ShortLinkCountRespVO>> listCountShortLink(@RequestParam("requestParam") List<String> requestParam){
        return ResultUtils.success(shortLinkService.listCountShortLink(requestParam) ,"查询成功！");
    }
}
