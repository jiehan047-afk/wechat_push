package com.example.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 日志切面类
 * 用于统一记录接口的入参、出参和耗时
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);
    
    /**
     * 定义切点，拦截所有Controller层的方法
     */
    @Pointcut("execution(* com.example.controller.*.*(..))")
    public void controllerPointcut() {
    }
    
    /**
     * 环绕通知，用于记录接口的入参、出参和耗时
     * @param joinPoint 连接点
     * @return 方法执行结果
     * @throws Throwable 异常信息
     */
    @Around("controllerPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 开始时间
        long startTime = System.currentTimeMillis();
        
        // 获取请求信息
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        
        // 获取方法信息
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = method.getName();
        
        // 获取请求参数
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = methodSignature.getParameterNames();
        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            params.put(parameterNames[i], args[i]);
        }
        
        // 记录请求信息
        logger.info("【接口请求】" +
                "\n请求URL：{}"
                + "\n请求方法：{}"
                + "\n请求IP：{}"
                + "\n处理器：{}.{}"
                + "\n请求参数：{}",
                request.getRequestURL().toString(),
                request.getMethod(),
                request.getRemoteAddr(),
                className,
                methodName,
                params);
        
        // 执行方法
        Object result = joinPoint.proceed();
        
        // 结束时间
        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;
        
        // 记录响应信息
        logger.info("【接口响应】" +
                "\n处理器：{}.{}"
                + "\n响应结果：{}"
                + "\n执行时间：{}ms",
                className,
                methodName,
                result,
                costTime);
        
        return result;
    }
}