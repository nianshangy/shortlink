package com.nian.shortlink.admin.common.convention.errorcode;

public enum UserErrorCode implements IErrorCode {

    USER_NULL("B000001","用户不存在"),
    USER_EXIST("B000100", "用户名已存在");

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
