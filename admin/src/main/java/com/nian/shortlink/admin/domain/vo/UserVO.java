package com.nian.shortlink.admin.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nian.shortlink.admin.common.serialize.PhoneDesensitizationSerializer;
import com.nian.shortlink.admin.common.serialize.UsernameDesensitizationSerializer;
import lombok.Data;

@Data
public class UserVO {
    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 真实姓名
     */
    @TableField(value = "real_name")
    @JsonSerialize(using = UsernameDesensitizationSerializer.class)
    private String real_name;

    /**
     * 手机号
     */
    @TableField(value = "phone")
    @JsonSerialize(using = PhoneDesensitizationSerializer.class)
    private String phone;

    /**
     * 邮箱
     */
    @TableField(value = "mail")
    private String mail;
}
