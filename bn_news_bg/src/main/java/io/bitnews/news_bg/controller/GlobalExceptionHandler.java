package io.bitnews.news_bg.controller;

import io.bienews.common.helper.exception.CommonException;
import io.bitnews.common.constants.CommonBNErrorCode;
import io.bitnews.common.model.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    public static final String GLOBAL_ERROR_CODE = "100000";

    @ExceptionHandler(value = CommonException.class)
    @ResponseBody
    public BaseResponse defaultErrorHandler(CommonException e) {
        log.error("统一异常处理", e);
        return new BaseResponse(new CommonBNErrorCode(GLOBAL_ERROR_CODE, e.getMessage(), e.getMessage()));
    }
}