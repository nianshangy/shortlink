package com.nian.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nian.shortlink.admin.common.convention.errorcode.UserErrorCode;
import com.nian.shortlink.admin.common.convention.exception.ClientException;
import com.nian.shortlink.admin.domain.req.user.UserLoginReqDTO;
import com.nian.shortlink.admin.domain.req.user.UserRegisterReqDTO;
import com.nian.shortlink.admin.domain.req.user.UserUpdateReqDTO;
import com.nian.shortlink.admin.domain.entity.User;
import com.nian.shortlink.admin.domain.resp.user.UserLoginRespVO;
import com.nian.shortlink.admin.domain.resp.user.UserRespVO;
import com.nian.shortlink.admin.mapper.UserMapper;
import com.nian.shortlink.admin.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.apache.shardingsphere.sharding.exception.metadata.DuplicatedIndexException;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.nian.shortlink.admin.common.constat.RedisCacheConstant.LOCK_USER_REGISTER_KEY;

/**
 * @description 针对表【t_user】的数据库操作Service实现
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final RBloomFilter<String> userRegisterCacheBloomFilter;
    private final RedissonClient redissonClient;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public UserRespVO queryByUsername(String username) {
        User user = lambdaQuery().eq(User::getUsername, username).one();
        if (user == null) {
            throw new ClientException("输入的用户不存在！", UserErrorCode.USER_NULL);
        }
        UserRespVO result = new UserRespVO();
        BeanUtils.copyProperties(user, result);
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
    public void userRegister(UserRegisterReqDTO requestParam) {
        //1.判断用户是否存在
        if (!hasUsername(requestParam.getUsername())) {
            throw new ClientException(UserErrorCode.USER_NAME_EXIST);
        }
        //2.根据用户名去获取分布式锁(注意：后面还需要加上用户名，不然就是所以用户获取同一把锁了)
        RLock lock = redissonClient.getLock(LOCK_USER_REGISTER_KEY + requestParam.getUsername());
        try {
            if (lock.tryLock()) {
                //2.新增到数据库
                try {
                    int insert = baseMapper.insert(BeanUtil.toBean(requestParam, User.class));
                    if (insert < 1) {
                        throw new ClientException(UserErrorCode.USER_SAVE_ERROR);
                    }
                } catch (DuplicatedIndexException e) {
                    throw new ClientException(UserErrorCode.USER_EXIST);
                }
                //3.每次注册用户时需要去将用户名存储进布隆过滤器中
                userRegisterCacheBloomFilter.add(requestParam.getUsername());
                return;
            }
            throw new ClientException(UserErrorCode.USER_NAME_EXIST);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void userUpdate(UserUpdateReqDTO requestParam) {
        //TODO 验证当前用户是否与登录用户一致
        LambdaUpdateWrapper<User> updateWrapper = Wrappers.lambdaUpdate(User.class).eq(User::getUsername, requestParam.getUsername());
        boolean updated = update(BeanUtil.toBean(requestParam, User.class), updateWrapper);
        if(!updated){
            throw new ClientException("修改用户信息失败",UserErrorCode.USER_UPDATE_ERROR);
        }
    }

    @Override
    public UserLoginRespVO userLogin(UserLoginReqDTO requestParam) {
        //1.从数据库中查询用户信息
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery(User.class)
                .eq(User::getUsername, requestParam.getUsername())
                .eq(User::getPassword, requestParam.getPassword())
                .eq(User::getDel_flag, 0);
        //2.判断用户是否存在
        if(queryWrapper == null){
            throw new ClientException(UserErrorCode.USER_NULL);
        }
        //3.判断redis中用户是否存在
        Boolean hasLogin = stringRedisTemplate.hasKey("login_" + requestParam.getUsername());
        if(hasLogin != null && hasLogin){
           throw new ClientException("用户已登录");
        }
        //4.保存到redis中
        //4.1随机生成UUID作为token
        String uuid = UUID.randomUUID().toString();
        //4.2将UUID作为key去保存到redis
        stringRedisTemplate.opsForHash().put("login_" +
                requestParam.getUsername(),uuid, JSON.toJSONString(requestParam));
        stringRedisTemplate.expire("login_" + requestParam.getUsername(),30L, TimeUnit.DAYS);
        return new UserLoginRespVO(uuid);

        //下面这个方法会让一个用户可以无限次注册
        /* //3.2将UUID作为key去保存到redis
        stringRedisTemplate.opsForValue().set(uuid, JSON.toJSONString(requestParam),30L, TimeUnit.MINUTES);*/
    }

    @Override
    public Boolean userCheckLogin(String username,String token) {
        return stringRedisTemplate.opsForHash().hasKey("login_" + username,token);
    }

    @Override
    public void userLogout(String username,String token) {
        //TODO 验证退出用户名是否与当前登录用户名一致
        if(userCheckLogin(username,token)){
            stringRedisTemplate.delete("login_" + username);
            return;
        }
        throw new ClientException("用户token不存在或用户未登录");
    }
}