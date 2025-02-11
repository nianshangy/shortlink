package com.nian.shortlink.admin.remote;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
import com.nian.shortlink.admin.remote.resp.link.ShortLinkBatchCreateRespVO;
import com.nian.shortlink.admin.remote.resp.link.ShortLinkCountRespVO;
import com.nian.shortlink.admin.remote.resp.link.ShortLinkCreateRespVO;
import com.nian.shortlink.admin.remote.resp.link.ShortLinkPageRespVO;
import com.nian.shortlink.admin.remote.resp.linkStats.ShortLinkAccessRecordRespVO;
import com.nian.shortlink.admin.remote.resp.linkStats.ShortLinkStatsRespVO;
import com.nian.shortlink.admin.remote.resp.recycle.ShortLinkRecycleBinPageRespVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//已废弃，改成feign远程调用
/**
 * ShortLink远程调用服务
 */
public interface ShortLinkRemoteService {

    /**
     * 创建短链接
     */
    default Result<ShortLinkCreateRespVO> createShortLink(ShortLinkCreateReqDTO requestParam){
        String resultCreateStr = HttpUtil.post("http://127.0.0.1:8001/api/short-link/v1/create", JSON.toJSONString(requestParam));
        //Result中的Data是个泛型，如果直接传结果的话，JSON反序列化时，JSON不知道这个是什么东西，那么就需要new TypeReference<>() {}去做一层转换，把resultCreateStr的类型传给它
        return JSON.parseObject(resultCreateStr, new TypeReference<>() {});
    }

    /**
     * 批量创建短链接
     */
    default Result<ShortLinkBatchCreateRespVO> batchCreateShortLink(ShortLinkBatchCreateReqDTO requestParam){
        String resultCreateStr = HttpUtil.post("http://127.0.0.1:8001/api/short-link/v1/create/batch", JSON.toJSONString(requestParam));
        //Result中的Data是个泛型，如果直接传结果的话，JSON反序列化时，JSON不知道这个是什么东西，那么就需要new TypeReference<>() {}去做一层转换，把resultCreateStr的类型传给它
        return JSON.parseObject(resultCreateStr, new TypeReference<>() {});
    }


    /**
     * 修改短链接
     */
    default void updateShortLink(ShortLinkUpdateReqDTO requestParam){
       HttpUtil.post("http://127.0.0.1:8001/api/short-link/v1/update",JSON.toJSONString(requestParam));
    }

    /**
     * 短链接分页查寻
     */
     default Result<IPage<ShortLinkPageRespVO>> pageShortLink(ShortLinkPageDTO requestParam){
        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("gid",requestParam.getGid());
        requestMap.put("orderTag",requestParam.getOrderTag());
        requestMap.put("current",requestParam.getCurrent());
        requestMap.put("size",requestParam.getSize());
        String resultPageStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/v1/page", requestMap);
        return JSON.parseObject(resultPageStr, new TypeReference<>() {});
    }

    /**
     * 查询短链接分组内数量
     */
    default Result<List<ShortLinkCountRespVO>> listCountShortLink(List<String> requestParam){
        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("requestParam",requestParam);
        String resultCountStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/v1/count", requestMap);
        return JSON.parseObject(resultCountStr, new TypeReference<>() {});
    }

    /**
     * 根据 url 查询网站标题
     */
    default Result<String> getTitleByUrl(String url){
        String resultStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/v1/title?url=" + url);
        return JSON.parseObject(resultStr, new TypeReference<>() {});
    }

    /**
     * 保存到回收站
     */
    default void saveRecycleBin(RecycleBinSaveReqDTO requestParam){
        HttpUtil.post("http://127.0.0.1:8001/api/short-link/v1/recycle/save",JSON.toJSONString(requestParam));
    }


    /**
     * 分页查寻回收站短链接
     */
    default Result<Page<ShortLinkRecycleBinPageRespVO>> pageRecycleShortLink(List<String> gidList, Long current, Long size){
        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("gidList",gidList);
        requestMap.put("current",current);
        requestMap.put("size",size);
        String resultPageStr = HttpUtil.post("http://127.0.0.1:8001/api/short-link/v1/recycle/page", requestMap);
        return JSON.parseObject(resultPageStr, new TypeReference<>() {});
    }

    /**
     * 恢复短链接
     */
    default void recoverRecycleBin(RecycleBinRecoverReqDTO requestParam) {
        HttpUtil.post("http://127.0.0.1:8001/api/short-link/v1/recycle/recover",JSON.toJSONString(requestParam));
    }

    /**
     * 移除短链接
     */
    default void removeRecycleBin(RecycleBinRemoveReqDTO requestParam) {
        HttpUtil.post("http://127.0.0.1:8001/api/short-link/v1/recycle/remove",JSON.toJSONString(requestParam));
    }


    //短链接监控

    /**
     * 访问单个短链接指定时间内监控数据
     */
    default Result<ShortLinkStatsRespVO> oneShortLinkStats(ShortLinkStatsReqDTO requestParam){
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(requestParam);
        String resultBodyStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/v1/stats", stringObjectMap);
        return JSON.parseObject(resultBodyStr, new TypeReference<>() {});
    }

    /**
     * 访问分组短链接指定时间内监控数据
     */
    default Result<ShortLinkStatsRespVO> groupShortLinkStats(ShortLinkGroupStatsReqDTO requestParam){
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(requestParam);
        String resultBodyStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/v1/stats/group", stringObjectMap);
        return JSON.parseObject(resultBodyStr, new TypeReference<>() {});
    }

    /**
     * 访问单个短链接指定时间内监控访问记录数据
     */
    default Result<IPage<ShortLinkAccessRecordRespVO>> shortLinkStatsAccessRecord(ShortLinkAccessRecordReqDTO requestParam){
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(requestParam);
        stringObjectMap.remove("orders");
        stringObjectMap.remove("records");
        String resultBodyStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/v1/stats/access-record", stringObjectMap);
        return JSON.parseObject(resultBodyStr, new TypeReference<>() {});
    }

    /**
     * 访问分组短链接指定时间内监控访问记录数据
     */
    default Result<IPage<ShortLinkAccessRecordRespVO>> groupShortLinkStatsAccessRecord(ShortLinkGroupAccessRecordReqDTO requestParam){
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(requestParam);
        stringObjectMap.remove("orders");
        stringObjectMap.remove("records");
        String resultBodyStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/v1/stats/access-record/group", stringObjectMap);
        return JSON.parseObject(resultBodyStr, new TypeReference<>() {});
    }

}
