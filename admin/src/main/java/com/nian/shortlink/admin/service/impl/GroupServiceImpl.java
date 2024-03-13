package com.nian.shortlink.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nian.shortlink.admin.common.convention.exception.ClientException;
import com.nian.shortlink.admin.domain.entity.Group;
import com.nian.shortlink.admin.mapper.GroupMapper;
import com.nian.shortlink.admin.service.IGroupService;
import com.nian.shortlink.admin.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 短链接分组接口实现层
 */
@Service
@RequiredArgsConstructor

public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements IGroupService {

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
        String gid;
        do {
            gid = RandomUtil.randomString(6);
        } while (hasGid(null, gid));
        Group group = Group.builder()
                .gid(gid)
                .sortOrder(0)
                .username(/*username*/null)
                .name(groupName)
                .build();
        baseMapper.insert(group);
    }

    private boolean hasGid(String username, String gid) {
        LambdaQueryWrapper<Group> queryWrapper = Wrappers.lambdaQuery(Group.class)
                .eq(Group::getGid, gid)
                //TODO 待设置用户名
                .eq(Group::getUsername, null /*Optional.ofNullable(username).orElse(UserContext.getUsername())*/);
        Group hasGroupFlag = baseMapper.selectOne(queryWrapper);
        return hasGroupFlag != null;
    }
}
