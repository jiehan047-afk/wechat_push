package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户绑定请求DTO
 * 用于接收用户工号和微信openid的绑定请求
 */
@Data
@Schema(name = "BindRequest", description = "用户绑定请求DTO")
public class BindRequest {
    /**
     * 用户工号
     */
    @Schema(description = "用户工号", required = true, example = "user123")
    private String userId;
    
    /**
     * 微信openid
     */
    @Schema(description = "微信openid", required = true, example = "oABC123456789")
    private String openId;
}