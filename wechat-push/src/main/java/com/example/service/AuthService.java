package com.example.service;

import com.example.dto.LoginResponse;

/**
 * 认证服务接口
 * 定义登录相关的业务逻辑
 */
public interface AuthService {
    
    /**
     * 用户登录
     * @param userId 用户工号
     * @return 登录响应，包含token和用户信息
     */
    LoginResponse login(String userId);
    
    /**
     * 验证token
     * @param token JWT token
     * @return 是否有效
     */
    boolean validateToken(String token);
    
    /**
     * 从token中获取用户工号
     * @param token JWT token
     * @return 用户工号
     */
    String getUserIdFromToken(String token);
}
