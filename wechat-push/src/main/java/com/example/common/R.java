package com.example.common;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 统一返回结果类
 * 用于规范API接口的返回格式
 * @param <T> 泛型参数，用于指定返回数据的类型
 */
@Data
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 状态码
     * 0：成功
     * 非0：失败
     */
    private int code;
    
    /**
     * 消息
     */
    private String message;
    
    /**
     * 返回的数据
     */
    private T data;
    
    /**
     * 时间戳
     */
    private String timestamp;
    
    /**
     * 构造函数
     */
    private R(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    /**
     * 成功返回结果
     * @param <T> 泛型参数
     * @return R<T>实例
     */
    public static <T> R<T> success() {
        return new R<>(0, "success", null);
    }
    
    /**
     * 成功返回结果
     * @param <T> 泛型参数
     * @param data 返回的数据
     * @return R<T>实例
     */
    public static <T> R<T> success(T data) {
        return new R<>(0, "success", data);
    }
    
    /**
     * 成功返回结果
     * @param <T> 泛型参数
     * @param message 消息
     * @param data 返回的数据
     * @return R<T>实例
     */
    public static <T> R<T> success(String message, T data) {
        return new R<>(0, message, data);
    }
    
    /**
     * 失败返回结果
     * @param <T> 泛型参数
     * @return R<T>实例
     */
    public static <T> R<T> error() {
        return new R<>(500, "error", null);
    }
    
    /**
     * 失败返回结果
     * @param <T> 泛型参数
     * @param message 错误消息
     * @return R<T>实例
     */
    public static <T> R<T> error(String message) {
        return new R<>(500, message, null);
    }
    
    /**
     * 失败返回结果
     * @param <T> 泛型参数
     * @param code 错误码
     * @param message 错误消息
     * @return R<T>实例
     */
    public static <T> R<T> error(int code, String message) {
        return new R<>(code, message, null);
    }
    
    /**
     * 失败返回结果
     * @param <T> 泛型参数
     * @param code 错误码
     * @param message 错误消息
     * @param data 返回的数据
     * @return R<T>实例
     */
    public static <T> R<T> error(int code, String message, T data) {
        return new R<>(code, message, data);
    }
}