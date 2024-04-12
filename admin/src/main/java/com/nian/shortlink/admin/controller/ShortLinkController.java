package com.nian.shortlink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nian.shortlink.admin.common.convention.result.Result;
import com.nian.shortlink.admin.common.convention.result.ResultUtils;
import com.nian.shortlink.admin.remote.ShortLinkRemoteService;
import com.nian.shortlink.admin.remote.req.link.ShortLinkBatchCreateReqDTO;
import com.nian.shortlink.admin.remote.req.link.ShortLinkCreateReqDTO;
import com.nian.shortlink.admin.remote.req.link.ShortLinkPageDTO;
import com.nian.shortlink.admin.remote.req.link.ShortLinkUpdateReqDTO;
import com.nian.shortlink.admin.remote.resp.link.*;
import com.nian.shortlink.admin.utils.EasyExcelWebUtil;
import jakarta.servlet.http.HttpServletResponse;
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
    @PostMapping("/api/short-link/admin/v1/create/batch")
    public void batchCreateShortLink(@RequestBody ShortLinkBatchCreateReqDTO requestParam, HttpServletResponse response){
        Result<ShortLinkBatchCreateRespVO> shortLinkBatchCreateRespVOResult = shortLinkRemoteService.batchCreateShortLink(requestParam);
        if (shortLinkBatchCreateRespVOResult.isSuccess()){
            List<ShortLinkBatchBaseInfoRespVO> baseLinkInfos = shortLinkBatchCreateRespVOResult.getData().getBaseLinkInfos();
            //将返回结果通过easyExcel发送给前端
            EasyExcelWebUtil.write(response,"批量创建短链接-京东短链接系统",ShortLinkBatchBaseInfoRespVO.class,baseLinkInfos);
        }
    }

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
