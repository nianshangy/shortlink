package com.nian.shortlink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nian.shortlink.admin.common.convention.result.Result;
import com.nian.shortlink.admin.common.convention.result.ResultUtils;
import com.nian.shortlink.admin.remote.ShortLinkRemoteService;
import com.nian.shortlink.admin.remote.req.ShortLinkCreateReqDTO;
import com.nian.shortlink.admin.remote.req.ShortLinkPageDTO;
import com.nian.shortlink.admin.remote.req.ShortLinkUpdateReqDTO;
import com.nian.shortlink.admin.remote.resp.ShortLinkCountRespVO;
import com.nian.shortlink.admin.remote.resp.ShortLinkCreateRespVO;
import com.nian.shortlink.admin.remote.resp.ShortLinkPageRespVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * 批量创建短链接
     */
    /*@PostMapping("/api/short-link/v1/create/batch")
    public void batchCreateShortLink(@RequestBody ShortLinkBatchCreateReqDTO requestParam){
        *//*ShortLinkBatchCreateRespVO shortLinkBatchCreateRespVOResult = shortLinkService.batchCreateShortLink(requestParam);
        //将返回结果通过easyExcel发送给前端
        return ResultUtils.success(shortLinkBatchCreateRespVOResult,"创建短链接成功！");*//*
        Result<ShortLinkBatchCreateRespVO> shortLinkBatchCreateRespVOResult = shortLinkService.batchCreateShortLink(requestParam);
        if (shortLinkBatchCreateRespVOResult.isSuccess()){

        }
        //将返回结果通过easyExcel发送给前端
    }*/

    /**
     * 修改短链接
     */
    @PutMapping("/api/short-link/admin/v1/update")
    public Result<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam){
        shortLinkRemoteService.updateShortLink(requestParam);
        return ResultUtils.success("修改短链接成功！");
    }

    /**
     * 短链接分页查寻
     */
    @GetMapping("/api/short-link/admin/v1/page")
    public Result<IPage<ShortLinkPageRespVO>> pageShortLink(ShortLinkPageDTO requestParam){
        return shortLinkRemoteService.pageShortLink(requestParam);
    }

    /**
     * 查询短链接分组内数量
     */
    @GetMapping("/api/short-link/admin/v1/count")
    public Result<List<ShortLinkCountRespVO>> listCountShortLink(@RequestParam("requestParam") List<String> requestParam) {
        return shortLinkRemoteService.listCountShortLink(requestParam);
    }
}
