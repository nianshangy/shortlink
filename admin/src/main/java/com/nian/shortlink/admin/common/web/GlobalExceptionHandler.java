package com.nian.shortlink.admin.common.web;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.nian.shortlink.admin.common.convention.errorcode.BaseErrorCode;
import com.nian.shortlink.admin.common.convention.exception.AbstractException;
import com.nian.shortlink.admin.common.convention.result.Result;
import com.nian.shortlink.admin.common.convention.result.ResultUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

/**
 * 全局异常处理器
 */
@Component("globalExceptionHandlerByAdmin")
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 拦截参数验证异常
     */
    @SneakyThrows
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result validExceptionHandler(HttpServletRequest request, MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        FieldError firstFieldError = CollectionUtil.getFirst(bindingResult.getFieldErrors());
        String exceptionStr = Optional.ofNullable(firstFieldError)
                .map(FieldError::getDefaultMessage)
                .orElse(StrUtil.EMPTY);
        log.error("[{}] {} [ex] {}", request.getMethod(), getUrl(request), exceptionStr);
        return ResultUtils.failure(BaseErrorCode.CLIENT_ERROR.code(), exceptionStr);
    }

    /**
     * 拦截应用内抛出的异常
     */
    @ExceptionHandler(value = {AbstractException.class})
    public Result abstractException(HttpServletRequest request, AbstractException ex) {
        if (ex.getCause() != null) {
            log.error("[{}] {} [ex] {}", request.getMethod(), request.getRequestURL().toString(), ex.toString(), ex.getCause());
            return ResultUtils.failure(ex);
        }
        log.error("[{}] {} [ex] {}", request.getMethod(), request.getRequestURL().toString(), ex.toString());
        return ResultUtils.failure(ex);
    }

    /**
     * 拦截未捕获异常
     */
    public Result defaultErrorHandler(HttpServletRequest request, Throwable throwable) {
        log.error("[{}] {} ", request.getMethod(), getUrl(request), throwable);
        return ResultUtils.failure();
    }

    private String getUrl(HttpServletRequest request) {
        if (StringUtils.isEmpty(request.getQueryString())) {
            return request.getRequestURL().toString();
        }
        return request.getRequestURL().toString() + "?" + request.getQueryString();
    }
}
