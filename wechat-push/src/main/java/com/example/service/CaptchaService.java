package com.example.service;

/**
 * 验证码服务接口
 * 负责验证码的生成、发送、校验等功能
 */
public interface CaptchaService {

    /**
     * 发送验证码
     * 根据工号获取邮箱，生成验证码并发送
     * @param userId 用户工号
     * @param openId 微信openId（可选，用于暂存绑定信息）
     * @return 发送结果邮箱（脱敏）
     */
    String sendCaptcha(String userId, String openId);

    /**
     * 验证验证码
     * @param userId 用户工号
     * @param captcha 用户输入的验证码
     * @return 验证是否通过
     */
    boolean verifyCaptcha(String userId, String captcha);

    /**
     * 获取暂存的openId
     * @param userId 用户工号
     * @return 暂存的openId
     */
    String getStoredOpenId(String userId);

    /**
     * 清除验证码和暂存信息
     * @param userId 用户工号
     */
    void clearCaptcha(String userId);
}
