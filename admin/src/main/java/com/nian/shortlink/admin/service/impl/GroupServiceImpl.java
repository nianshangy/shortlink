package com.nian.shortlink.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nian.shortlink.admin.domain.entity.Group;
import com.nian.shortlink.admin.mapper.GroupMapper;
import com.nian.shortlink.admin.service.IGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 短链接分组接口实现层
 */
@Service
@RequiredArgsConstructor

public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements IGroupService {

    @Override
    public void saveGroup(String groupName) {

    }
}
