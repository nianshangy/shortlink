package com.nian.shortlink.admin.domain.vo;

import lombok.Data;

/**
 * 用户无脱敏信息返回参数
 */
@Data
public class UserActualRespVO {
    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String mail;
}

