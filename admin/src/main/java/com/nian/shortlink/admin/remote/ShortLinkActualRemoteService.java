package com.nian.shortlink.admin.remote;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nian.shortlink.admin.common.convention.result.Result;
import com.nian.shortlink.admin.remote.req.link.ShortLinkBatchCreateReqDTO;
import com.nian.shortlink.admin.remote.req.link.ShortLinkCreateReqDTO;
import com.nian.shortlink.admin.remote.req.link.ShortLinkPageDTO;
import com.nian.shortlink.admin.remote.req.link.ShortLinkUpdateReqDTO;
import com.nian.shortlink.admin.remote.req.linkStats.ShortLinkAccessRecordReqDTO;
import com.nian.shortlink.admin.remote.req.linkStats.ShortLinkGroupAccessRecordReqDTO;
import com.nian.shortlink.admin.remote.req.linkStats.ShortLinkGroupStatsReqDTO;
import com.nian.shortlink.admin.remote.req.linkStats.ShortLinkStatsReqDTO;
import com.nian.shortlink.admin.remote.req.recycle.RecycleBinRecoverReqDTO;
import com.nian.shortlink.admin.remote.req.recycle.RecycleBinRemoveReqDTO;
import com.nian.shortlink.admin.remote.req.recycle.RecycleBinSaveReqDTO;
import com.nian.shortlink.admin.remote.req.recycle.ShortLinkRecycleBinPageReqDTO;
import com.nian.shortlink.admin.remote.resp.link.ShortLinkBatchCreateRespVO;
import com.nian.shortlink.admin.remote.resp.link.ShortLinkCountRespVO;
import com.nian.shortlink.admin.remote.resp.link.ShortLinkCreateRespVO;
import com.nian.shortlink.admin.remote.resp.link.ShortLinkPageRespVO;
import com.nian.shortlink.admin.remote.resp.linkStats.ShortLinkAccessRecordRespVO;
import com.nian.shortlink.admin.remote.resp.linkStats.ShortLinkStatsRespVO;
import com.nian.shortlink.admin.remote.resp.recycle.ShortLinkRecycleBinPageRespVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * ShortLink远程调用服务
 */
@FeignClient(value = "short-link-project" , url = "${aggregation.remote-url:}")
public interface ShortLinkActualRemoteService {

    //短链接有关操作
    /**
     * 创建短链接
     */
    @PostMapping("/api/short-link/v1/create")
    Result<ShortLinkCreateRespVO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam);

    /**
     * 批量创建短链接
     */
    @PostMapping("/api/short-link/v1/create/batch")
    Result<ShortLinkBatchCreateRespVO> batchCreateShortLink(@RequestBody ShortLinkBatchCreateReqDTO requestParam);

    /**
     * 修改短链接
     */
    @PostMapping("/api/short-link/v1/update")
    void updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam);

    /**
     * 短链接分页查寻
     */
    @GetMapping("/api/short-link/v1/page")
    Result<Page<ShortLinkPageRespVO>> pageShortLink(@SpringQueryMap ShortLinkPageDTO requestParam);

    /**
     * 查询短链接分组内数量
     */
    @GetMapping("/api/short-link/v1/count")
    Result<List<ShortLinkCountRespVO>> listCountShortLink(@RequestParam("requestParam") List<String> requestParam);

    /**
     * 根据 url 查询网站标题
     */
    @GetMapping("/api/short-link/v1/title")
    Result<String> getTitleByUrl(@RequestParam("url") String url);


    //短链接回收站有关操作

    /**
     * 保存到回收站
     */
    @PostMapping("/api/short-link/v1/recycle/save")
    void saveRecycleBin(@RequestBody RecycleBinSaveReqDTO requestParam);

    /**
     * 分页查寻回收站短链接
     */
    @GetMapping("/api/short-link/v1/recycle/page")
    Result<Page<ShortLinkRecycleBinPageRespVO>> pageRecycleShortLink(@SpringQueryMap ShortLinkRecycleBinPageReqDTO requestParam);

    /**
     * 恢复短链接
     */
    @PostMapping("/api/short-link/v1/recycle/recover")
    void recoverRecycleBin(@RequestBody RecycleBinRecoverReqDTO requestParam);

    /**
     * 移除短链接
     */
    @PostMapping("/api/short-link/v1/recycle/remove")
    void removeRecycleBin(@RequestBody RecycleBinRemoveReqDTO requestParam);


    //短链接监控

    /**
     * 访问单个短链接指定时间内监控数据
     */
    @GetMapping("/api/short-link/v1/stats")
    Result<ShortLinkStatsRespVO> oneShortLinkStats(@SpringQueryMap ShortLinkStatsReqDTO requestParam);

    /**
     * 访问分组短链接指定时间内监控数据
     */
    @GetMapping("/api/short-link/v1/stats/group")
    Result<ShortLinkStatsRespVO> groupShortLinkStats(@SpringQueryMap ShortLinkGroupStatsReqDTO requestParam);

    /**
     * 访问单个短链接指定时间内监控访问记录数据
     */
    @GetMapping("/api/short-link/v1/stats/access-record")
    Result<Page<ShortLinkAccessRecordRespVO>> shortLinkStatsAccessRecord(@SpringQueryMap ShortLinkAccessRecordReqDTO requestParam);

    /**
     * 访问分组短链接指定时间内监控访问记录数据
     */
    @GetMapping("/api/short-link/v1/stats/access-record/group")
    Result<Page<ShortLinkAccessRecordRespVO>> groupShortLinkStatsAccessRecord(@SpringQueryMap ShortLinkGroupAccessRecordReqDTO requestParam);
}
