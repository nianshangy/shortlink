package com.nian.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nian.shortlink.admin.domain.req.group.GroupDeleteReqDTO;
import com.nian.shortlink.admin.domain.req.group.GroupSortReqDTO;
import com.nian.shortlink.admin.domain.req.group.GroupUpdateReqDTO;
import com.nian.shortlink.admin.domain.entity.Group;
import com.nian.shortlink.admin.domain.resp.group.GroupQueryListRespVO;

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
     * 新增短链接分组
     * @param username 用户名
     * @param groupName 短链接组名
     */
    void groupSave(String username,String groupName);

    /**
     * 查询短链接分组集合
     * @return 查询短链接分组集合返回参数
     */
    List<GroupQueryListRespVO> queryList();

    /**
     * 更新短链接分组名称
     * @param requestParam 更新短链接分组名称参数
     */
    void updateGroup(GroupUpdateReqDTO requestParam);

    /**
     * 删除短链接分组
     * @param requestParam 删除短链接分组参数
     */
    void deleteGroup(GroupDeleteReqDTO requestParam);

    /**
     * 短链接分组排序
     * @param requestParam 短链接分组排序参数
     */
    void sortGroup(List<GroupSortReqDTO> requestParam);
}
