package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 登录响应DTO
 * 用于返回登录成功后的token和用户信息
 */
@Data
@Schema(description = "登录响应DTO")
public class LoginResponse {
    /**
     * JWT token
     */
    @Schema(description = "JWT token")
    private String token;

    /**
     * 用户工号
     */
    @Schema(description = "用户工号")
    private String userId;

    /**
     * 微信openid（如果已绑定）
     */
    @Schema(description = "微信openid（如果已绑定）")
    private String openId;
}
