package com.example.controller;

import com.example.common.R;
import com.example.dto.BindRequest;
import com.example.dto.BindWithCaptchaRequest;
import com.example.dto.SendCaptchaRequest;
import com.example.entity.UserBind;
import com.example.service.CaptchaService;
import com.example.service.UserBindService;
import com.example.service.WeChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户绑定控制器
 * 提供绑定用户工号和微信openid的接口
 */
@RestController
@RequestMapping("/api/user-bind")
@Tag(name = "用户绑定", description = "用户工号和微信openid的绑定接口")
public class UserBindController {
    private static final Logger logger = LoggerFactory.getLogger(UserBindController.class);

    @Autowired
    private UserBindService userBindService;

    @Autowired
    private WeChatService weChatService;

    @Autowired
    private CaptchaService captchaService;
    
    /**
     * 生成微信授权URL
     * @param redirectUri 回调URL
     * @param state 状态参数
     * @return 授权URL
     */
    @GetMapping("/wx-auth-url")
    @Operation(summary = "生成微信授权URL", description = "生成用于微信授权的URL，用于获取用户的openid")
    public R<Map<String, String>> generateWxAuthUrl(
            @Parameter(description = "授权成功后的回调URL", required = true) @RequestParam String redirectUri,
            @Parameter(description = "状态参数，用于防止CSRF攻击") @RequestParam(required = false) String state) {
        logger.info("生成微信授权URL，回调地址: {}", redirectUri);
        String authUrl = weChatService.generateOAuth2Url(redirectUri, state != null ? state : "");
        return R.success(Map.of("authUrl", authUrl));
    }
    
    /**
     * 微信授权回调接口
     * @param code 授权code
     * @param state 状态参数
     * @return openid
     */
    @GetMapping("/wx-callback")
    @Operation(summary = "微信授权回调", description = "微信授权成功后的回调接口，用于获取用户的openid")
    public R<Map<String, String>> wxCallback(
            @Parameter(description = "微信授权code", required = true) @RequestParam String code,
            @Parameter(description = "状态参数，与请求时保持一致") @RequestParam(required = false) String state) {
        logger.info("收到微信授权回调，code: {}, state: {}", code, state);
        String openId = weChatService.getOpenIdByCode(code);
        return R.success(Map.of("openId", openId, "state", state != null ? state : ""));
    }
    
    /**
     * 绑定用户工号和微信openid
     * @param request 包含userId和openId的请求体
     * @return 绑定结果
     */
    @PostMapping("/bind")
    @Operation(summary = "绑定用户工号和微信openid", description = "将用户的工号与微信openid进行绑定")
    public R<UserBind> bindUser(
            @Parameter(description = "绑定请求，包含用户工号和微信openid", required = true) @RequestBody BindRequest request) {
        logger.info("绑定用户请求: userId={}, openId={}", request.getUserId(), request.getOpenId());
        UserBind userBind = userBindService.bindUser(request.getUserId(), request.getOpenId());
        return R.success(userBind);
    }

    /**
     * 发送验证码
     * 根据工号获取邮箱并发送验证码
     * @param request 包含userId和可选openId的请求体
     * @return 发送结果（脱敏邮箱）
     */
    @PostMapping("/send-captcha")
    @Operation(summary = "发送验证码", description = "根据工号获取邮箱并发送验证码，验证码5分钟内有效")
    public R<Map<String, String>> sendCaptcha(
            @Parameter(description = "发送验证码请求，包含用户工号", required = true) @RequestBody SendCaptchaRequest request) {
        logger.info("发送验证码请求: userId={}", request.getUserId());
        String maskedEmail = captchaService.sendCaptcha(request.getUserId(), request.getOpenId());
        return R.success(Map.of(
                "message", "验证码已发送",
                "email", maskedEmail
        ));
    }

    /**
     * 通过验证码绑定用户
     * @param request 包含userId、openId和captcha的请求体
     * @return 绑定结果
     */
    @PostMapping("/bind-with-captcha")
    @Operation(summary = "验证码绑定", description = "通过验证码验证后绑定用户工号和微信openid")
    public R<UserBind> bindWithCaptcha(
            @Parameter(description = "验证码绑定请求", required = true) @RequestBody BindWithCaptchaRequest request) {
        logger.info("验证码绑定请求: userId={}", request.getUserId());
        UserBind userBind = userBindService.bindWithCaptcha(
                request.getUserId(),
                request.getOpenId(),
                request.getCaptcha()
        );
        return R.success(userBind);
    }

    /**
     * 根据openId查询绑定用户
     * @param openId 微信openid
     * @return 绑定信息
     */
    @GetMapping("/get-by-openid")
    @Operation(summary = "根据openId查询绑定用户", description = "根据微信openid查询绑定的用户工号")
    public R<Map<String, String>> getByOpenId(
            @Parameter(description = "微信openid", required = true) @RequestParam String openId) {
        logger.info("根据openId查询绑定用户: openId={}", openId);
        return userBindService.findByOpenId(openId)
                .map(userBind -> R.success(Map.of(
                        "userId", userBind.getUserId(),
                        "openId", userBind.getOpenId()
                )))
                .orElse(R.error("未绑定工号"));
    }
}