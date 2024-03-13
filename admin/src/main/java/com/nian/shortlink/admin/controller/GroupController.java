package com.nian.shortlink.admin.controller;

import com.nian.shortlink.admin.common.convention.result.Result;
import com.nian.shortlink.admin.common.convention.result.ResultUtils;
import com.nian.shortlink.admin.domain.dto.group.GroupSaveReqDTO;
import com.nian.shortlink.admin.domain.vo.group.GroupQueryListRespVO;
import com.nian.shortlink.admin.service.IGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
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
    @PostMapping("/api/short-link/v1/group")
    public Result<Void> saveGroup(@RequestBody GroupSaveReqDTO requestParam){
        groupService.groupSave(requestParam.getGroupName());
        return ResultUtils.success("创建短链接分组成功");
    }

    /**
     * 查询短链接分组集合
     */
    @GetMapping("/api/short-link/v1/group")
    public Result<List<GroupQueryListRespVO>> queryList(){
        return ResultUtils.success(groupService.queryList(),"查询成功");
    }
}
