package com.nian.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nian.shortlink.admin.common.convention.errorcode.UserErrorCode;
import com.nian.shortlink.admin.common.convention.exception.ClientException;
import com.nian.shortlink.admin.domain.dto.UserDTO;
import com.nian.shortlink.admin.domain.entity.User;
import com.nian.shortlink.admin.domain.vo.UserVO;
import com.nian.shortlink.admin.mapper.UserMapper;
import com.nian.shortlink.admin.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
* @author nianshang
* @description 针对表【t_user】的数据库操作Service实现
* @createDate 2024-03-03 20:05:57
*/
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements IUserService {

    private final RBloomFilter<String> userRegisterCacheBloomFilter;

    @Override
    public UserVO queryByUsername(String username) {
        User user = lambdaQuery().eq(User::getUsername, username).one();
        if(user == null){
            throw new ClientException("输入的用户不存在！",UserErrorCode.USER_NULL);
        }
        UserVO result = new UserVO();
        BeanUtils.copyProperties(user,result);
        return result;
    }

    @Override
    public Boolean hasUsername(String username) {
        //直接通过布隆过滤器去判断用户是否存在
        //如果布隆过滤器存在username，说明不可以用,就返回false
        return !userRegisterCacheBloomFilter.contains(username);

        /*LambdaQueryWrapper<User> queryWrappe = Wrappers.lambdaQuery(User.class)
                .eq(User::getUsername, username);
        User user = baseMapper.selectOne(queryWrappe);
        return user != null;*/
    }

    @Override
    public void userRegister(UserDTO requestParam) {
        //1.判断用户是否存在
        if(!hasUsername(requestParam.getUsername())){
            throw new ClientException(UserErrorCode.USER_NAME_EXIST);
        }
        //2.新增到数据库
        User user = new User();
        BeanUtil.copyProperties(requestParam,user);
        boolean save = save(user);
        if(!save){
            throw new ClientException(UserErrorCode.USER_SAVE_ERROR);
        }
        //3.每次注册用户时需要去将用户名存储进布隆过滤器中
        userRegisterCacheBloomFilter.add(requestParam.getUsername());
    }
}