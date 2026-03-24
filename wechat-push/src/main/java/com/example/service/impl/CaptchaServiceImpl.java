package com.example.service.impl;

import com.example.constant.Constant;
import com.example.service.CaptchaService;
import com.example.service.EmailService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * 验证码服务实现类
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {
    private static final Logger logger = LoggerFactory.getLogger(CaptchaServiceImpl.class);

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private EmailService emailService;

    private final SecureRandom random = new SecureRandom();

    @Override
    public String sendCaptcha(String userId, String openId) {
        logger.info("发送验证码请求: userId={}, openId={}", userId, openId);

        // 1. 根据工号获取邮箱
        String email = emailService.getEmailByUserId(userId);
        if (email == null || email.isEmpty()) {
            throw new RuntimeException("未找到该工号对应的邮箱");
        }

        // 2. 检查发送频率限制
        String limitKey = MessageFormat.format(Constant.Captcha.REDIS_KEY_CAPTCHA_LIMIT, userId);
        if (Boolean.TRUE.equals(redisTemplate.hasKey(limitKey))) {
            Long ttl = redisTemplate.getExpire(limitKey, TimeUnit.SECONDS);
            throw new RuntimeException("验证码发送频繁，请" + ttl + "秒后重试");
        }

        // 3. 生成验证码
        String captcha = generateCaptcha();

        // 4. 存入Redis
        String codeKey = MessageFormat.format(Constant.Captcha.REDIS_KEY_CAPTCHA_CODE, userId);
        String bindKey = MessageFormat.format(Constant.Captcha.REDIS_KEY_CAPTCHA_BIND, userId);

        redisTemplate.opsForValue().set(codeKey, captcha, 
                Constant.Captcha.CAPTCHA_EXPIRE_MINUTES, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(limitKey, "1", 
                Constant.Captcha.CAPTCHA_LIMIT_SECONDS, TimeUnit.SECONDS);

        // 暂存openId（如果提供）
        if (openId != null && !openId.isEmpty()) {
            redisTemplate.opsForValue().set(bindKey, openId, 
                    Constant.Captcha.CAPTCHA_EXPIRE_MINUTES, TimeUnit.MINUTES);
        }

        logger.info("验证码已生成并存入Redis: userId={}, captcha={}", userId, captcha);

        // 5. 发送邮件
        boolean sendResult = emailService.sendCaptchaEmail(email, captcha, userId);
        if (!sendResult) {
            // 发送失败，清除Redis中的验证码
            redisTemplate.delete(codeKey);
            redisTemplate.delete(limitKey);
            redisTemplate.delete(bindKey);
            throw new RuntimeException("验证码发送失败，请稍后重试");
        }

        // 6. 返回脱敏邮箱
        return maskEmail(email);
    }

    @Override
    public boolean verifyCaptcha(String userId, String captcha) {
        logger.info("验证验证码: userId={}, captcha={}", userId, captcha);

        String codeKey = MessageFormat.format(Constant.Captcha.REDIS_KEY_CAPTCHA_CODE, userId);
        String storedCaptcha = redisTemplate.opsForValue().get(codeKey);

        if (StringUtils.isBlank(storedCaptcha)) {
            throw new RuntimeException("验证码已过期，请重新获取");
        }

        if (!storedCaptcha.equals(captcha)) {
            logger.warn("验证码错误: userId={}, input={}, stored={}", userId, captcha, storedCaptcha);
            return false;
        }

        logger.info("验证码验证成功: userId={}", userId);
        return true;
    }

    @Override
    public String getStoredOpenId(String userId) {
        String bindKey = MessageFormat.format(Constant.Captcha.REDIS_KEY_CAPTCHA_BIND, userId);
        return redisTemplate.opsForValue().get(bindKey);
    }

    @Override
    public void clearCaptcha(String userId) {
        String codeKey = MessageFormat.format(Constant.Captcha.REDIS_KEY_CAPTCHA_CODE, userId);
        String bindKey = MessageFormat.format(Constant.Captcha.REDIS_KEY_CAPTCHA_BIND, userId);
        String limitKey = MessageFormat.format(Constant.Captcha.REDIS_KEY_CAPTCHA_LIMIT, userId);

        redisTemplate.delete(codeKey);
        redisTemplate.delete(bindKey);
        redisTemplate.delete(limitKey);

        logger.info("验证码信息已清除: userId={}", userId);
    }

    /**
     * 生成指定长度的数字验证码
     */
    private String generateCaptcha() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Constant.Captcha.CAPTCHA_LENGTH; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * 邮箱脱敏处理
     * 例如: zhangsan@company.com -> zha****@company.com
     */
    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }
        String[] parts = email.split("@");
        String name = parts[0];
        String domain = parts[1];

        if (name.length() <= 3) {
            return name.charAt(0) + "****@" + domain;
        } else {
            return name.substring(0, 3) + "****@" + domain;
        }
    }
}
