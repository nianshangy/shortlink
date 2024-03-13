package com.nian.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nian.shortlink.admin.domain.entity.Group;
import com.nian.shortlink.admin.domain.vo.group.GroupQueryListRespVO;

import java.util.List;

/**
 * 短链接分组接口
 */
public interface IGroupService extends IService<Group> {

    /**
     * 新增短链接分组
     * @param groupName 短链接组名
     */
    void groupSave(String groupName);

    /**
     * 查询短链接分组集合
     * @return 查询短链接分组集合返回参数
     */
    List<GroupQueryListRespVO> queryList();
}
