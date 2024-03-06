package com.nian.shortlink.admin.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class UserActualVO {
    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

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

