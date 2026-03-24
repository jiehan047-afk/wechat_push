package com.example.service;

/**
 * 邮件服务接口
 * 负责调用第三方邮件发送接口
 */
public interface EmailService {

    /**
     * 根据工号获取邮箱地址
     * @param userId 用户工号
     * @return 邮箱地址
     */
    String getEmailByUserId(String userId);

    /**
     * 发送验证码邮件
     * @param email 邮箱地址
     * @param captcha 验证码
     * @param userId 用户工号
     * @return 是否发送成功
     */
    boolean sendCaptchaEmail(String email, String captcha, String userId);
}
