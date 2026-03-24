package com.example.controller;

import com.example.common.R;
import com.example.dto.LoginRequest;
import com.example.dto.LoginResponse;
import com.example.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证控制器
 * 处理登录相关的API请求
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "认证", description = "登录相关的API接口")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    /**
     * 用户登录
     * @param request 登录请求，包含用户工号
     * @return 登录响应，包含token和用户信息
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "通过用户工号进行登录，返回JWT token")
    public R<LoginResponse> login(
            @Parameter(description = "登录请求，包含用户工号", required = true) @RequestBody LoginRequest request) {
        logger.info("登录请求: userId={}", request.getUserId());
        LoginResponse response = authService.login(request.getUserId());
        return R.success(response);
    }
}
