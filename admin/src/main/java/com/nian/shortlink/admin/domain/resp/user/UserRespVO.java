package com.nian.shortlink.admin.domain.resp.user;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nian.shortlink.admin.common.serialize.PhoneDesensitizationSerializer;
import com.nian.shortlink.admin.common.serialize.UsernameDesensitizationSerializer;
import lombok.Data;

/**
 * 用户脱敏信息返回参数
 */
@Data
public class UserRespVO {
    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    @JsonSerialize(using = UsernameDesensitizationSerializer.class)
    private String realName;

    /**
     * 手机号
     */
    @JsonSerialize(using = PhoneDesensitizationSerializer.class)
    private String phone;

    /**
     * 邮箱
     */
    private String mail;
}
