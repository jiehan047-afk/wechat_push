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

        // 验证用户工号（这里简化处理，假设所有工号都有效）
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("用户工号不能为空");
        }

        // 检查用户是否已绑定微信openid
        String openId = userBindService.findByUserId(userId)
                .map(UserBind::getOpenId)
                .orElse(null);

        // 生成JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        if (openId != null) {
            claims.put("openId", openId);
        }

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
