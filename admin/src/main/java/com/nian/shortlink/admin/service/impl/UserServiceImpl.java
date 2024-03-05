package com.nian.shortlink.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nian.shortlink.admin.common.convention.errorcode.UserErrorCode;
import com.nian.shortlink.admin.common.convention.exception.ClientException;
import com.nian.shortlink.admin.common.convention.result.Result;
import com.nian.shortlink.admin.common.convention.result.ResultUtils;
import com.nian.shortlink.admin.dao.entity.User;
import com.nian.shortlink.admin.domain.vo.UserVO;
import com.nian.shortlink.admin.service.IUserService;
import com.nian.shortlink.admin.dao.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
* @author nianshang
* @description 针对表【t_user】的数据库操作Service实现
* @createDate 2024-03-03 20:05:57
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements IUserService {

    @Override
    public Result<UserVO> queryByUsername(String username) {
        User user = lambdaQuery().eq(User::getUsername, username).one();
        if(user == null){
            throw new ClientException("输入的用户不存在！",UserErrorCode.USER_NULL);
        }
        UserVO result = new UserVO();
        BeanUtils.copyProperties(user,result);
        return ResultUtils.success(result);
    }
}