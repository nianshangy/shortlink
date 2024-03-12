package com.nian.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nian.shortlink.admin.domain.dto.user.UserLoginReqDTO;
import com.nian.shortlink.admin.domain.dto.user.UserRegisterReqDTO;
import com.nian.shortlink.admin.domain.dto.user.UserUpdateReqDTO;
import com.nian.shortlink.admin.domain.entity.User;
import com.nian.shortlink.admin.domain.vo.UserLoginRespVO;
import com.nian.shortlink.admin.domain.vo.UserRespVO;

/**
* @author nianshang
* @description 针对表【t_user】的数据库操作Service
* @createDate 2024-03-03 20:05:57
*/
public interface IUserService extends IService<User> {

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return UserVO
     */
    UserRespVO queryByUsername(String username);

    /**
     * 查询用户是否存在
     * @param username 用户名
     * @return Boolean
     */
    Boolean hasUsername(String username);

    /**
     * 注册用户
     * requestParam 用户注册请求参数
     */
    void userRegister(UserRegisterReqDTO requestParam);

    /**
     * 根据用户名修改用户信息
     * @param requestParam 用户修改请求参数
     */
    void userUpdate(UserUpdateReqDTO requestParam);

    /**
     * 用户登录
     * @param requestParam 用户登录请求参数
     * @return 用户登录返回参数 token
     */
    UserLoginRespVO userLogin(UserLoginReqDTO requestParam);

    /**
     * 检查用户是否登录
     * @param token 用户登录token
     * @return 是否登录
     */
    Boolean userCheckLogin(String username,String token);

    /**
     * 用户退出登录
     * @param username 用户名
     */
    void userLogout(String username,String token);
}
