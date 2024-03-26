package com.nian.shortlink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nian.shortlink.project.common.convention.result.Result;
import com.nian.shortlink.project.common.convention.result.ResultUtils;
import com.nian.shortlink.project.domain.req.RecycleBinSaveReqDTO;
import com.nian.shortlink.project.domain.req.ShortLinkRecycleBinPageReqDTO;
import com.nian.shortlink.project.domain.resp.ShortLinkRecycleBinPageRespVO;
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
}
