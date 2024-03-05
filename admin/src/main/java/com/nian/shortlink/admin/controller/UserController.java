package com.nian.shortlink.admin.controller;

import com.nian.shortlink.admin.common.convention.result.Result;
import com.nian.shortlink.admin.domain.vo.UserVO;
import com.nian.shortlink.admin.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/api/shortlink/v1/user/{username}")
    public Result<UserVO> getUserByUserName(@PathVariable("username") String username){
        return userService.queryByUsername(username);
    }
}
