package com.nian.shortlink.admin.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class UserDTO {
    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 真实姓名
     */
    @TableField(value = "real_name")
    private String real_name;

    /**
     * 手机号
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField(value = "mail")
    private String mail;
}
