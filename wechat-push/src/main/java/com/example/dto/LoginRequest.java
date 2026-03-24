package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 登录请求DTO
 * 用于接收登录请求参数
 */
@Data
@Schema(description = "登录请求DTO")
public class LoginRequest {
    /**
     * 用户工号
     */
    @Schema(description = "用户工号", required = true)
    private String userId;
}
