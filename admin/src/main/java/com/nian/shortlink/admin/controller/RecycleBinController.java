package com.nian.shortlink.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nian.shortlink.admin.common.convention.result.Result;
import com.nian.shortlink.admin.common.convention.result.ResultUtils;
import com.nian.shortlink.admin.remote.ShortLinkRemoteService;
import com.nian.shortlink.admin.remote.req.recycle.RecycleBinRecoverReqDTO;
import com.nian.shortlink.admin.remote.req.recycle.RecycleBinRemoveReqDTO;
import com.nian.shortlink.admin.remote.req.recycle.RecycleBinSaveReqDTO;
import com.nian.shortlink.admin.remote.req.recycle.ShortLinkRecycleBinPageReqDTO;
import com.nian.shortlink.admin.remote.resp.recycle.ShortLinkRecycleBinPageRespVO;
import com.nian.shortlink.admin.service.IRecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 回收站控制层
 */
@RestController
@RequiredArgsConstructor
public class RecycleBinController {

    ShortLinkRemoteService shortLinkRemoteService= new ShortLinkRemoteService(){};
    private final IRecycleBinService recycleBinService;

    /**
     * 保存到回收站
     */
    @PostMapping("/api/short-link/admin/v1/recycle/save")
    public Result<Void> saveRecycleBin(@RequestBody RecycleBinSaveReqDTO requestParam){
        shortLinkRemoteService.saveRecycleBin(requestParam);
        return ResultUtils.success("成功添加进回收站");
    }

    /**
     * 分页查寻回收站短链接
     */
    @PostMapping("/api/short-link/admin/v1/recycle/page")
    public Result<Page<ShortLinkRecycleBinPageRespVO>> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam){
        return recycleBinService.pageRecycleShortLink(requestParam);
    }

    /**
     * 恢复短链接
     */
    @PostMapping("/api/short-link/admin/v1/recycle/recover")
    public Result<Void> recoverRecycleBin(@RequestBody RecycleBinRecoverReqDTO requestParam) {
        shortLinkRemoteService.recoverRecycleBin(requestParam);
        return ResultUtils.success("成功从回收站恢复短链接");
    }

    /**
     * 移除短链接
     */
    @PostMapping("/api/short-link/admin/v1/recycle/remove")
    public Result<Void> removeRecycleBin(@RequestBody RecycleBinRemoveReqDTO requestParam) {
        shortLinkRemoteService.removeRecycleBin(requestParam);
        return ResultUtils.success("成功从回收站移除短链接");
    }
}
