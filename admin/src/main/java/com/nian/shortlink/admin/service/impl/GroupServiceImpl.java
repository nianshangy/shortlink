
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
import com.nian.shortlink.admin.domain.req.group.GroupSortReqDTO;
import com.nian.shortlink.admin.domain.req.group.GroupUpdateReqDTO;
import com.nian.shortlink.admin.domain.resp.group.GroupQueryListRespVO;
import com.nian.shortlink.admin.mapper.GroupMapper;
import com.nian.shortlink.admin.remote.ShortLinkActualRemoteService;
import com.nian.shortlink.admin.remote.resp.link.ShortLinkCountRespVO;
import com.nian.shortlink.admin.service.IGroupService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.nian.shortlink.admin.common.constat.RedisCacheConstant.LOCK_GROUP_SAVE_KEY;

/**
 * 短链接分组接口实现层
 */
@Service
@RequiredArgsConstructor
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements IGroupService {

    private final ShortLinkActualRemoteService shortLinkActualRemoteService;
    private final RedissonClient redissonClient;

    @Value("${short-link.group.max-num}")
    private Integer groupMaxNum;


    @Override
    public void groupSave(String groupName) {
        groupSave(UserContext.getUsername(),groupName);
    }

    @Override
    public void groupSave(String username, String groupName) {
        //这是为了防止同一用户在不同线程（可能同一用户在多个设备）同时新增分组，而超出最大分组数吧。
        RLock lock = redissonClient.getLock(String.format(LOCK_GROUP_SAVE_KEY, username));
        lock.lock();
        try {
            //判断该用户下分组是否大于20个
            LambdaQueryWrapper<Group> queryWrapper = Wrappers.lambdaQuery(Group.class)
                    .eq(Group::getUsername, username)
                    .eq(Group::getDelFlag, 0);
            List<Group> groupList = baseMapper.selectList(queryWrapper);
            if(groupList != null && groupList.size() > groupMaxNum){
                throw new ClientException(String.format("已超出最大分组数：%d", groupMaxNum));
            }
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
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<GroupQueryListRespVO> queryList() {
        LambdaQueryWrapper<Group> queryWrapper = Wrappers.lambdaQuery(Group.class)
                .eq(Group::getDelFlag, 0)
                .eq(Group::getUsername, UserContext.getUsername())
                .orderByDesc(Group::getSortOrder, Group::getUpdateTime);
        List<Group> groups = baseMapper.selectList(queryWrapper);
        List<GroupQueryListRespVO> groupQueryListRespVOList = BeanUtil.copyToList(groups, GroupQueryListRespVO.class);
        List<ShortLinkCountRespVO> linkCountRespDTO = shortLinkActualRemoteService.listCountShortLink(groups.stream().map(Group::getGid).toList()).getData();
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
                .eq(Group::getDelFlag, 0);
        Group group = new Group();
        group.setName(requestParam.getGroupName());
        baseMapper.update(group, updateWrapper);
    }

    @Override
    public void deleteGroup(String gid) {
        LambdaUpdateWrapper<Group> updateWrapper = Wrappers.lambdaUpdate(Group.class)
                .eq(Group::getDelFlag, 0)
                .eq(Group::getUsername, UserContext.getUsername())
                .eq(Group::getGid, gid);
        Group group = new Group();
        group.setDelFlag(1);
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
                    .eq(Group::getDelFlag, 0);
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
