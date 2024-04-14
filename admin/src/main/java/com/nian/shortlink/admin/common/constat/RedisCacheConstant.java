package com.nian.shortlink.admin.common.constat;

/**
 * 短链接后管项目 redis 的缓存常量类
 */
public class RedisCacheConstant {

    /**
     * 用户注册分布式锁
     */
    public static final String LOCK_USER_REGISTER_KEY = "short-link:lock_user_register:";

    /**
     * 创建分组分布式锁
     */
    public static final String LOCK_GROUP_SAVE_KEY = "short-link:lock_group_save:%s";
}
