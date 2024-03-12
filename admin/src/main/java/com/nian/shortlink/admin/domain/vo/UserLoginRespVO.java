package com.nian.shortlink.admin.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录返回响应
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRespVO {
    /**
     * 用户token
     */
    private String token;
}
