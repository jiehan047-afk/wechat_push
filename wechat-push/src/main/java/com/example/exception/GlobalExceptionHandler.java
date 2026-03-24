package com.example.exception;

import com.example.common.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 全局异常处理器
 * 用于统一处理系统中的异常，并返回统一的R<T>格式
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * 处理GlobalException异常
     * @param e 异常对象
     * @param request 请求对象
     * @return 统一返回结果
     */
    @ExceptionHandler(GlobalException.class)
    public R<Object> handleGlobalException(GlobalException e, HttpServletRequest request) {
        logger.error("GlobalException occurred: {}", e.getMessage(), e);
        
        // 记录异常详情
        logExceptionDetails(e, request);
        
        return R.error(e.getCode(), e.getMessage());
    }
    
    /**
     * 处理RuntimeException异常
     * @param e 异常对象
     * @param request 请求对象
     * @return 统一返回结果
     */
    @ExceptionHandler(RuntimeException.class)
    public R<Object> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        logger.error("RuntimeException occurred: {}", e.getMessage(), e);
        
        // 记录异常详情
        logExceptionDetails(e, request);
        
        return R.error(500, e.getMessage());
    }
    
    /**
     * 处理Exception异常
     * @param e 异常对象
     * @param request 请求对象
     * @return 统一返回结果
     */
    @ExceptionHandler(Exception.class)
    public R<Object> handleException(Exception e, HttpServletRequest request) {
        logger.error("Exception occurred: {}", e.getMessage(), e);
        
        // 记录异常详情
        logExceptionDetails(e, request);
        
        return R.error(500, "服务器内部错误");
    }
    
    /**
     * 记录异常详情
     * @param e 异常对象
     * @param request 请求对象
     */
    private void logExceptionDetails(Exception e, HttpServletRequest request) {
        String requestUrl = request.getRequestURL().toString();
        String requestMethod = request.getMethod();
        
        // 获取异常堆栈信息
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTrace = sw.toString();
        
        logger.error("异常详情：" +
                "\n请求URL：{}"
                + "\n请求方法：{}"
                + "\n异常类型：{}"
                + "\n异常消息：{}"
                + "\n堆栈信息：{}",
                requestUrl, requestMethod,
                e.getClass().getName(), e.getMessage(), stackTrace);
    }
}