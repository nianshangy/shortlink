package com.nian.shortlink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nian.shortlink.project.common.convention.result.Result;
import com.nian.shortlink.project.common.convention.result.ResultUtils;
import com.nian.shortlink.project.domain.req.recycle.RecycleBinRecoverReqDTO;
import com.nian.shortlink.project.domain.req.recycle.RecycleBinRemoveReqDTO;
import com.nian.shortlink.project.domain.req.recycle.RecycleBinSaveReqDTO;
import com.nian.shortlink.project.domain.req.recycle.ShortLinkRecycleBinPageReqDTO;
import com.nian.shortlink.project.domain.resp.recycle.ShortLinkRecycleBinPageRespVO;
import com.nian.shortlink.project.service.IRecycleBinService;
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

    private final IRecycleBinService recycleBinService;

    /**
     * 保存到回收站
     */
    @PostMapping("/api/short-link/v1/recycle/save")
    public Result<Void> saveRecycleBin(@RequestBody RecycleBinSaveReqDTO requestParam){
        recycleBinService.saveRecycleBin(requestParam);
        return ResultUtils.success("成功添加进回收站");
    }

    /**
     * 分页查寻回收站短链接
     */
    @PostMapping("/api/short-link/v1/recycle/page")
    public Result<IPage<ShortLinkRecycleBinPageRespVO>> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam){
        return ResultUtils.success(recycleBinService.pageRecycleShortLink(requestParam),"查询成功！");
    }

    /**
     * 恢复短链接
     */
    @PostMapping("/api/short-link/v1/recycle/recover")
    public Result<Void> recoverRecycleBin(@RequestBody RecycleBinRecoverReqDTO requestParam) {
        recycleBinService.recoverRecycleBin(requestParam);
        return ResultUtils.success("恢复短链接成功");
    }

    /**
     * 移除短链接
     */
    @PostMapping("/api/short-link/v1/recycle/remove")
    public Result<Void> removeRecycleBin(@RequestBody RecycleBinRemoveReqDTO requestParam) {
        recycleBinService.removeRecycleBin(requestParam);
        return ResultUtils.success("移除短链接成功");
    }
}
