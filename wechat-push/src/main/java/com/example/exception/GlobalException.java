package com.example.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 全局异常类
 * 用于封装业务异常，方便在业务逻辑中抛出和处理
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GlobalException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    /**
     * 错误码
     */
    private int code;
    
    /**
     * 错误消息
     */
    private String message;
    
    /**
     * 构造函数
     * @param message 错误消息
     */
    public GlobalException(String message) {
        super(message);
        this.code = 500;
        this.message = message;
    }
    
    /**
     * 构造函数
     * @param code 错误码
     * @param message 错误消息
     */
    public GlobalException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
    
    /**
     * 构造函数
     * @param code 错误码
     * @param message 错误消息
     * @param cause 异常原因
     */
    public GlobalException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }
}