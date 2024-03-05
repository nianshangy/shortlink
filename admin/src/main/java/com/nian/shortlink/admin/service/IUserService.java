package com.nian.shortlink.admin.service;

import com.nian.shortlink.admin.common.convention.result.Result;
import com.nian.shortlink.admin.dao.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nian.shortlink.admin.domain.vo.UserVO;

/**
* @author nianshang
* @description 针对表【t_user】的数据库操作Service
* @createDate 2024-03-03 20:05:57
*/
public interface IUserService extends IService<User> {

    Result<UserVO> queryByUsername(String username);
}
