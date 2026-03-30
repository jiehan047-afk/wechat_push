package com.example.service.impl;

import com.example.dto.LoginResponse;
import com.example.entity.UserBind;
import com.example.service.AuthService;
import com.example.service.UserBindService;
import com.example.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务实现类
 * 实现登录相关的业务逻辑
 */
@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserBindService userBindService;

    @Override
    public LoginResponse login(String userId) {
        logger.info("用户登录: {}", userId);

        // 验证用户工号
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("用户工号不能为空");
        }

        // 检查用户是否已绑定微信（只允许已绑定用户登录）
        UserBind userBind = userBindService.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户未绑定，请先通过邮箱绑定工号"));

        String openId = userBind.getOpenId();

        // 生成JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("openId", openId);

        String token = jwtUtil.generateToken(claims, userId);

        // 构建登录响应
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserId(userId);
        response.setOpenId(openId);

        logger.info("用户登录成功: {}", userId);
        return response;
    }

    @Override
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }

    @Override
    public String getUserIdFromToken(String token) {
        return jwtUtil.getSubjectFromToken(token);
    }
}
