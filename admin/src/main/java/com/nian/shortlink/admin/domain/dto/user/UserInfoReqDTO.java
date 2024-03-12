package com.nian.shortlink.admin.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息实体(将其保存到ThreadLocal中，方便使用的DTO)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoReqDTO {

    /**
     * 用户 ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 用户 Token
     */
    private String token;
}