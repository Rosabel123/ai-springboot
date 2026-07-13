package com.xxs.aispringbooot.common.exception;

import com.xxs.aispringbooot.common.Result;
import com.xxs.aispringbooot.common.enums.ResultCode;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //处理参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handleException(MethodArgumentNotValidException e) {
        //获取异常信息
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(","));
        return Result.error(ResultCode.PARAM_ERROR.getCode(), ResultCode.PARAM_ERROR.getMsg(), message);
    }
    //处理业务异常
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleException(BusinessException e) {
        return Result.error(e.getCode(), e.getMessage(), e.getData());
    }
}

