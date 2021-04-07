package io.bitnews.passport.controller;

import io.bienews.common.helper.exception.CommonException;
import io.bienews.common.helper.exception.UnauthorizedException;
import io.bitnews.common.constants.CommonBNErrorCode;
import io.bitnews.common.model.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ywd on 2019/8/29.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    public static final String GLOBAL_ERROR_CODE = "100000";

    @ExceptionHandler(value = CommonException.class)
    public BaseResponse defaultErrorHandler(CommonException e) {
        log.error("统一异常处理", e);
        return new BaseResponse(new CommonBNErrorCode(GLOBAL_ERROR_CODE, e.getMessage(), e.getMessage()));
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public BaseResponse paramsErrorHandler(IllegalArgumentException e) {
        log.error("参数异常处理", e);
        return new BaseResponse(new CommonBNErrorCode(GLOBAL_ERROR_CODE, e.getMessage(), e.getMessage()));
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BaseResponse handle401(UnauthorizedException e) {
        log.error("token异常处理", e);
        return new BaseResponse(new CommonBNErrorCode(GLOBAL_ERROR_CODE, e.getMessage(), e.getMessage()));
    }
}
