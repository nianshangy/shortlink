package com.nian.shortlink.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.nian.shortlink.admin.common.convention.result.Result;
import com.nian.shortlink.admin.common.convention.result.ResultUtils;
import com.nian.shortlink.admin.domain.dto.UserDTO;
import com.nian.shortlink.admin.domain.vo.UserActualVO;
import com.nian.shortlink.admin.domain.vo.UserVO;
import com.nian.shortlink.admin.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制层
 */
@RestController
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    /**
     * 根据用户名字查询用户信息
     */
    @GetMapping("/api/short-link/v1/user/{username}")
    public Result<UserVO> getUserByUserName(@PathVariable("username") String username){
        return ResultUtils.success(userService.queryByUsername(username));
    }

    /**
     * 根据用户名字查询用户真实未脱敏的信息
     */
    @GetMapping("/api/short-link/v1/actual/user/{username}")
    public Result<UserActualVO> getActualUserByUserName(@PathVariable("username") String username){
        return ResultUtils.success(BeanUtil.toBean(userService.queryByUsername(username), UserActualVO.class));
    }

    /**
     * 判断用户是否存在
     */
    @GetMapping("/api/short-link/v1/user/has-username")
    public Result<Boolean> hasUsername(@RequestParam("username") String username){
        return ResultUtils.success(userService.hasUsername(username));
    }

    /**
     * 用户注册
     */
    @PostMapping("/api/short-link/v1/user")
    private Result<Void> registerUser(@RequestBody UserDTO requestParam){
        userService.userRegister(requestParam);
        return ResultUtils.success();
    }
}
