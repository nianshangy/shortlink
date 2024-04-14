package com.nian.shortlink.admin.common.convention.errorcode;

public enum UserErrorCode implements IErrorCode {

    USER_NULL("B000001","用户不存在或密码错误"),
    USER_EXIST("B000100", "用户记录已存在"),
    USER_NAME_EXIST("B000101", "用户名已存在"),
    USER_SAVE_ERROR("B000201","保存用户失败"),
    USER_UPDATE_ERROR("B000202","修改用户失败");

    private final String code;

    private final String message;
    UserErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
