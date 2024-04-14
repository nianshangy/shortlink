package com.nian.shortlink.project.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.nian.shortlink.project.common.convention.result.Result;
import com.nian.shortlink.project.domain.req.link.ShortLinkCreateReqDTO;
import com.nian.shortlink.project.domain.resp.link.ShortLinkCreateRespVO;

/**
 * 自定义流控策略
 */
public class CustomBlockHandler {

    public static Result<ShortLinkCreateRespVO> createShortLinkBlockHandlerMethod(ShortLinkCreateReqDTO requestParam, BlockException exception){
        return new Result<ShortLinkCreateRespVO>().setCode("B100000").setMessage("当前访问人数较多，请稍后再试!");
    }
}
