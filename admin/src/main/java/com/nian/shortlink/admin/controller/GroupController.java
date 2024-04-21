package com.nian.shortlink.admin.controller;

import com.nian.shortlink.admin.common.convention.result.Result;
import com.nian.shortlink.admin.common.convention.result.ResultUtils;
import com.nian.shortlink.admin.domain.req.group.GroupSaveReqDTO;
import com.nian.shortlink.admin.domain.req.group.GroupSortReqDTO;
import com.nian.shortlink.admin.domain.req.group.GroupUpdateReqDTO;
import com.nian.shortlink.admin.domain.resp.group.GroupQueryListRespVO;
import com.nian.shortlink.admin.service.IGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 短链接分组控制层
 */
@RestController
@RequiredArgsConstructor
public class GroupController {

    private final IGroupService groupService;

    /**
     * 新增短链接分组
     */
    @PostMapping("/api/short-link/admin/v1/group")
    public Result<Void> saveGroup(@RequestBody GroupSaveReqDTO requestParam){
        groupService.groupSave(requestParam.getGroupName());
        return ResultUtils.success("创建短链接分组成功");
    }

    /**
     * 查询短链接分组集合
     */
    @GetMapping("/api/short-link/admin/v1/group")
    public Result<List<GroupQueryListRespVO>> queryList(){
        return ResultUtils.success(groupService.queryList(),"查询成功");
    }

    /**
     * 更新短链接分组名称
     */
    @PutMapping("/api/short-link/admin/v1/group")
    public Result<Void> updateGroup(@RequestBody GroupUpdateReqDTO requestParam){
        groupService.updateGroup(requestParam);
        return ResultUtils.success("修改成功");
    }

    /**
     * 删除短链接分组
     */
    @DeleteMapping("/api/short-link/admin/v1/group")
    public Result<Void> deleteGroup(@RequestParam String gid){
        groupService.deleteGroup(gid);
        return ResultUtils.success("删除成功");
    }

    /**
     * 短链接分组排序
     */
    @PostMapping("/api/short-link/admin/v1/group/sort")
    public Result<Void> sortGroup(@RequestBody List<GroupSortReqDTO> requestParam){
        groupService.sortGroup(requestParam);
        return ResultUtils.success("排序设置成功");
    }
}
