package com.nian.shortlink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nian.shortlink.admin.common.convention.result.Result;
import com.nian.shortlink.admin.common.convention.result.ResultUtils;
import com.nian.shortlink.admin.remote.ShortLinkRemoteService;
import com.nian.shortlink.admin.remote.req.RecycleBinSaveReqDTO;
import com.nian.shortlink.admin.remote.req.ShortLinkRecycleBinPageReqDTO;
import com.nian.shortlink.admin.remote.resp.ShortLinkRecycleBinPageRespVO;
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
    public Result<IPage<ShortLinkRecycleBinPageRespVO>> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam){
        return shortLinkRemoteService.pageRecycleShortLink(requestParam);
    }
}
