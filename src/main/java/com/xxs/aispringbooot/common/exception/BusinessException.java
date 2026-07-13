package com.xxs.aispringbooot.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final String message;
    private final String code;
    private final Object data;
    public BusinessException(String message) {
        super(message);
        this.message = message;
        this.code = "BUSINESS_ERROR";
        this.data = null;
    }
}
