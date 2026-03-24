package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 验证码绑定请求
 */
@Schema(description = "验证码绑定请求")
public class BindWithCaptchaRequest {

    @Schema(description = "用户工号", required = true, example = "10001")
    private String userId;

    @Schema(description = "微信openId", required = true, example = "oXXXX")
    private String openId;

    @Schema(description = "验证码", required = true, example = "123456")
    private String captcha;

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

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
