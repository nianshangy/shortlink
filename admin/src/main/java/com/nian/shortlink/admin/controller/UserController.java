package com.nian.shortlink.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.nian.shortlink.admin.common.convention.result.Result;
import com.nian.shortlink.admin.common.convention.result.ResultUtils;
import com.nian.shortlink.admin.domain.req.user.UserLoginReqDTO;
import com.nian.shortlink.admin.domain.req.user.UserRegisterReqDTO;
import com.nian.shortlink.admin.domain.req.user.UserUpdateReqDTO;
import com.nian.shortlink.admin.domain.resp.user.UserActualRespVO;
import com.nian.shortlink.admin.domain.resp.user.UserLoginRespVO;
import com.nian.shortlink.admin.domain.resp.user.UserRespVO;
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
    @GetMapping("/api/short-link/admin/v1/user/{username}")
    public Result<UserRespVO> getUserByUserName(@PathVariable("username") String username){
        return ResultUtils.success(userService.queryByUsername(username));
    }

    /**
     * 根据用户名字查询用户真实未脱敏的信息
     */
    @GetMapping("/api/short-link/admin/v1/actual/user/{username}")
    public Result<UserActualRespVO> getActualUserByUserName(@PathVariable("username") String username){
        return ResultUtils.success(BeanUtil.toBean(userService.queryByUsername(username), UserActualRespVO.class));
    }

    /**
     * 判断用户是否存在
     */
    @GetMapping("/api/short-link/admin/v1/user/has-username")
    public Result<Boolean> hasUsername(@RequestParam("username") String username){
        return ResultUtils.success(userService.hasUsername(username));
    }

    /**
     * 用户注册
     */
    @PostMapping("/api/short-link/admin/v1/user")
    private Result<Void> userRegister(@RequestBody UserRegisterReqDTO requestParam){
        userService.userRegister(requestParam);
        return ResultUtils.success("注册成功");
    }

    /**
     * 用户修改
     */
    @PutMapping("/api/short-link/admin/v1/user")
    private Result<Void> userUpdate(@RequestBody UserUpdateReqDTO requestParam){
        userService.userUpdate(requestParam);
        return ResultUtils.success("修改成功");
    }

    /**
     * 用户登录
     */
    @PostMapping("/api/short-link/admin/v1/user/login")
    public Result<UserLoginRespVO> userLogin(@RequestBody UserLoginReqDTO requestParam){
        return ResultUtils.success(userService.userLogin(requestParam),"登录成功");
    }

    /**
     * 检查用户是否登录
     */
    @GetMapping("/api/short-link/admin/v1/user/check-login")
    public Result<Boolean> userIsLogin(@RequestParam("username") String username,@RequestParam("token") String token){
        return ResultUtils.success(userService.userCheckLogin(username,token));
    }

    /**
     * 用户退出登录
     */
    @DeleteMapping("/api/short-link/admin/v1/user/login-out")
    public Result<Void> userLogout(@RequestParam("username") String username,@RequestParam("token") String token){
        userService.userLogout(username,token);
        return ResultUtils.success("退出登录成功");
    }
}
