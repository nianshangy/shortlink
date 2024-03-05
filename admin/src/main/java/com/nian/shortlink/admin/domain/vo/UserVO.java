package com.nian.shortlink.admin.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class UserVO {
    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

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
