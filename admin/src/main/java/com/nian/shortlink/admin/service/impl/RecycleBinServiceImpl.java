package com.nian.shortlink.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nian.shortlink.admin.common.biz.user.UserContext;
import com.nian.shortlink.admin.common.convention.exception.ServiceException;
import com.nian.shortlink.admin.common.convention.result.Result;
import com.nian.shortlink.admin.domain.entity.Group;
import com.nian.shortlink.admin.mapper.GroupMapper;
import com.nian.shortlink.admin.remote.ShortLinkRemoteService;
import com.nian.shortlink.admin.remote.req.ShortLinkRecycleBinPageReqDTO;
import com.nian.shortlink.admin.remote.resp.ShortLinkRecycleBinPageRespVO;
import com.nian.shortlink.admin.service.IRecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * URL 回收站接口实现层
 */
@Service
@RequiredArgsConstructor
public class RecycleBinServiceImpl implements IRecycleBinService {

    ShortLinkRemoteService shortLinkRemoteService= new ShortLinkRemoteService(){};
    private final GroupMapper groupMapper;

    @Override
    public Result<Page<ShortLinkRecycleBinPageRespVO>> pageRecycleShortLink(ShortLinkRecycleBinPageReqDTO requestParam) {
        LambdaQueryWrapper<Group> queryWrapper = Wrappers.lambdaQuery(Group.class)
                .eq(Group::getUsername, UserContext.getUser().getUsername())
                .eq(Group::getDelFlag, 0);
        List<Group> groupList = groupMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(groupList)) {
            throw new ServiceException("用户无分组信息");
        }
        requestParam.setGidList(groupList.stream().map(Group::getGid).collect(Collectors.toList()));
        return shortLinkRemoteService.pageRecycleShortLink(requestParam.getGidList(),requestParam.getCurrent(),requestParam.getSize());
    }
}
