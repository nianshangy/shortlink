package com.nian.shortlink.admin.domain.dto.user;

import lombok.Data;

@Data
public class UserLoginReqDTO {
    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;
}
