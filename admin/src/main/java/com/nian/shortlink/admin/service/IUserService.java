package com.nian.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nian.shortlink.admin.domain.entity.User;
import com.nian.shortlink.admin.domain.dto.UserDTO;
import com.nian.shortlink.admin.domain.vo.UserVO;

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
    UserVO queryByUsername(String username);

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
    void userRegister(UserDTO requestParam);
}
