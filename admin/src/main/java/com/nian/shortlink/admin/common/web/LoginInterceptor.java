package com.nian.shortlink.admin.common.web;

import cn.hutool.core.util.StrUtil;
import com.nian.shortlink.admin.utils.UserThreadLocal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {

    /**
     * boolean 表示是否放行
     * true:放行 用户可以跳转页面
     * false:拦截  之后给定重定向路径
     * <p>
     * 业务逻辑:
     * 1.判断用户客户端是否有Cookie/token数据
     * 如果用户没有token则重定向到用户登陆页面
     * 2.如果用户token中有数据,则从redis缓存中获取数据
     * 如果redis中数据为null,则重定向到用户登陆页面
     * 3.如果reids中有数据,则放行请求.
     */
    //执行controller方法之前执行
    //这个拦截器只是去将用户信息存储进ThreadLocal中的，不需要拦截返回访问，所以返回的都是true
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        //获取Cookie数据
        String username = request.getHeader("username");
        String token = request.getHeader("token");
        if (!StrUtil.isEmpty(token)) {
            /*//检测缓存中是否有该数据
            RedisCache redis = new RedisCache(redisTemplate);
            Object userinfo = redis.get(token);

            if (!StrUtil.isEmpty(userinfo)) {

                //将userJSON转化为User对象
                User user = (User) userinfo;

                UserThreadLocal.saveUser(user);
                //用户已经登陆 放行请求
                return true;
            }*/
            return true;        }
        //表示用户没有登陆
        return true;
    }

    //返回页面之前拦截
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        //将ThreadLocal数据清空
        UserThreadLocal.removeUser();
    }
}