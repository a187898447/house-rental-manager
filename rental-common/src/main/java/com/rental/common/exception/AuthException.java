package com.rental.common.exception;

/**
 * 认证异常
 */
public class AuthException extends RuntimeException {

    private final Integer code;

    public AuthException(String message) {
        super(message);
        this.code = 401;
    }

    public AuthException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
