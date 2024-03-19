package com.nian.shortlink.admin.remote;


import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nian.shortlink.admin.common.convention.result.Result;
import com.nian.shortlink.admin.remote.req.ShortLinkCreateReqDTO;
import com.nian.shortlink.admin.remote.req.ShortLinkPageDTO;
import com.nian.shortlink.admin.remote.resp.ShortLinkCountRespVO;
import com.nian.shortlink.admin.remote.resp.ShortLinkCreateRespVO;
import com.nian.shortlink.admin.remote.resp.ShortLinkPageRespVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 短链接分页查寻
     */
    default Result<IPage<ShortLinkPageRespVO>> pageShortLink(ShortLinkPageDTO requestParam){
        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("gid",requestParam.getGid());
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
}
