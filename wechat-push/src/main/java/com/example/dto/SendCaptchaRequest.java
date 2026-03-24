package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 发送验证码请求
 */
@Schema(description = "发送验证码请求")
public class SendCaptchaRequest {

    @Schema(description = "用户工号", required = true, example = "10001")
    private String userId;

    @Schema(description = "微信openId，可选，用于后续绑定", example = "oXXXX")
    private String openId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
