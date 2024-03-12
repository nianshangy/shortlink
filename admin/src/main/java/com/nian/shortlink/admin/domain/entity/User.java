package com.nian.shortlink.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.nian.shortlink.admin.common.database.DatabaseDO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName t_user
 */
@Data
@TableName(value ="t_user")
public class User extends DatabaseDO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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

    /**
     * 注销时间戳
     */
    @TableField(value = "deletion_time")
    private Long deletion_time;

}