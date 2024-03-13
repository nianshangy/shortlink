package com.nian.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nian.shortlink.admin.common.biz.user.UserContext;
import com.nian.shortlink.admin.domain.dto.group.GroupUpdateReqDTO;
import com.nian.shortlink.admin.domain.entity.Group;
import com.nian.shortlink.admin.domain.vo.group.GroupQueryListRespVO;
import com.nian.shortlink.admin.mapper.GroupMapper;
import com.nian.shortlink.admin.service.IGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

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
        } while (hasGid(/*UserContext.getUsername(),*/ gid));
        Group group = Group.builder()
                .gid(gid)
                .sortOrder(0)
                .username(UserContext.getUsername())
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
        return BeanUtil.copyToList(groups, GroupQueryListRespVO.class);
    }

    @Override
    public void updateGroup(GroupUpdateReqDTO requestParam) {
        LambdaUpdateWrapper<Group> updateWrapper = Wrappers.lambdaUpdate(Group.class)
                .eq(Group::getUsername, UserContext.getUsername())
                .eq(Group::getGid, requestParam.getGid())
                .eq(Group::getDel_flag, 0);
        Group group = new Group();
        group.setName(requestParam.getGroupName());
        baseMapper.update(group,updateWrapper);
    }

    private boolean hasGid(String gid) {
        LambdaQueryWrapper<Group> queryWrapper = Wrappers.lambdaQuery(Group.class)
                .eq(Group::getGid, gid)
                .eq(Group::getUsername, UserContext.getUsername()/*Optional.ofNullable(username).orElse(UserContext.getUsername())*/);
        Group hasGroupFlag = baseMapper.selectOne(queryWrapper);
        return hasGroupFlag != null;
    }
}
