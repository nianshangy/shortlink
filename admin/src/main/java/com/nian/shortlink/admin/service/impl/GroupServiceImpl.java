package com.nian.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nian.shortlink.admin.common.biz.user.UserContext;
import com.nian.shortlink.admin.common.convention.exception.ClientException;
import com.nian.shortlink.admin.domain.entity.Group;
import com.nian.shortlink.admin.domain.req.group.GroupDeleteReqDTO;
import com.nian.shortlink.admin.domain.req.group.GroupSortReqDTO;
import com.nian.shortlink.admin.domain.req.group.GroupUpdateReqDTO;
import com.nian.shortlink.admin.domain.resp.group.GroupQueryListRespVO;
import com.nian.shortlink.admin.mapper.GroupMapper;
import com.nian.shortlink.admin.remote.ShortLinkRemoteService;
import com.nian.shortlink.admin.remote.resp.ShortLinkCountRespVO;
import com.nian.shortlink.admin.service.IGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 短链接分组接口实现层
 */
@Service
@RequiredArgsConstructor

public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements IGroupService {

    ShortLinkRemoteService shortLinkRemoteService= new ShortLinkRemoteService(){};

    @Override
    public void groupSave(String groupName) {
        /*RLock lock = redissonClient.getLock(String.format(LOCK_GROUP_CREATE_KEY, username));
        lock.lock();
        try {
            LambdaQueryWrapper<Group> queryWrapper = Wrappers.lambdaQuery(Group.class)
                    .eq(Group::getUsername, username)
                    .eq(Group::getDelFlag, 0);
            List<Group> groupDOList = baseMapper.selectList(queryWrapper);
            if (CollUtil.isNotEmpty(groupDOList) && groupDOList.size() == groupMaxNum) {
                throw new ClientException(String.format("已超出最大分组数：%d", groupMaxNum));
            }
            String gid;
            do {
                gid = RandomGenerator.generateRandom();
            } while (!hasGid(username, gid));
            Group group = Group.builder()
                    .gid(gid)
                    .sortOrder(0)
                    .username(username)
                    .name(groupName)
                    .build();
            baseMapper.insert(group);
        } finally {
            lock.unlock();
        }*/
        groupSave(UserContext.getUsername(),groupName);
    }

    @Override
    public void groupSave(String username, String groupName) {
        String gid;
        do {
            gid = RandomUtil.randomString(6);
        } while (hasGid(username,gid));
        Group group = Group.builder()
                .gid(gid)
                .sortOrder(0)
                .username(username)
                .name(groupName)
                .build();
        baseMapper.insert(group);
    }

    @Override
    public List<GroupQueryListRespVO> queryList() {
        LambdaQueryWrapper<Group> queryWrapper = Wrappers.lambdaQuery(Group.class)
                .eq(Group::getDel_flag, 0)
                .eq(Group::getUsername, UserContext.getUsername())
                .orderByDesc(Group::getSortOrder, Group::getUpdate_time);
        List<Group> groups = baseMapper.selectList(queryWrapper);
        List<GroupQueryListRespVO> groupQueryListRespVOList = BeanUtil.copyToList(groups, GroupQueryListRespVO.class);
        List<ShortLinkCountRespVO> linkCountRespDTO = shortLinkRemoteService.listCountShortLink(groups.stream().map(Group::getGid).toList()).getData();
        Map<String, Integer> counts = linkCountRespDTO.stream().collect(Collectors.toMap(ShortLinkCountRespVO::getGid, ShortLinkCountRespVO::getShortLinkCount));
        groupQueryListRespVOList.forEach(each -> {
            each.setShortLinkCount(counts.get(each.getGid()));
        });
        return groupQueryListRespVOList;
    }

    @Override
    public void updateGroup(GroupUpdateReqDTO requestParam) {
        LambdaUpdateWrapper<Group> updateWrapper = Wrappers.lambdaUpdate(Group.class)
                .eq(Group::getUsername, UserContext.getUsername())
                .eq(Group::getGid, requestParam.getGid())
                .eq(Group::getDel_flag, 0);
        Group group = new Group();
        group.setName(requestParam.getGroupName());
        baseMapper.update(group, updateWrapper);
    }

    @Override
    public void deleteGroup(GroupDeleteReqDTO requestParam) {
        LambdaUpdateWrapper<Group> updateWrapper = Wrappers.lambdaUpdate(Group.class)
                .eq(Group::getDel_flag, 0)
                .eq(Group::getUsername, UserContext.getUsername())
                .eq(Group::getGid, requestParam.getGid());
        Group group = new Group();
        group.setDel_flag(1);
        int update = baseMapper.update(group, updateWrapper);
        if (update <= 0) {
            throw new ClientException("删除失败，数据已被删除或者数据不存在！");
        }
    }

    @Override
    public void sortGroup(List<GroupSortReqDTO> requestParam) {
        requestParam.forEach(each -> {
            Group group = new Group();
            group.setSortOrder(each.getSortOrder());

            LambdaUpdateWrapper<Group> updateWrapper = Wrappers.lambdaUpdate(Group.class)
                    .eq(Group::getUsername, UserContext.getUsername())
                    .eq(Group::getGid, each.getGid())
                    .eq(Group::getDel_flag, 0);
            baseMapper.update(group, updateWrapper);
        });
    }

    private boolean hasGid(String username,String gid) {
        LambdaQueryWrapper<Group> queryWrapper = Wrappers.lambdaQuery(Group.class)
                .eq(Group::getGid, gid)
                .eq(Group::getUsername, Optional.ofNullable(username).orElse(UserContext.getUsername()));
        Group hasGroupFlag = baseMapper.selectOne(queryWrapper);
        return hasGroupFlag != null;
    }
}
