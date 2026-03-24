package com.example.service.impl;

import com.example.entity.UserBind;
import com.example.repository.UserBindRepository;
import com.example.service.CaptchaService;
import com.example.service.UserBindService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 用户绑定服务实现类
 * 实现用户绑定的业务逻辑
 */
@Service
public class UserBindServiceImpl implements UserBindService {
    private static final Logger logger = LoggerFactory.getLogger(UserBindServiceImpl.class);

    @Autowired
    private UserBindRepository userBindRepository;

    @Autowired
    private CaptchaService captchaService;

    @Override
    public UserBind bindUser(String userId, String openId) {
        logger.info("开始绑定用户工号: {} 和微信openid: {}", userId, openId);
        
        // 检查用户工号是否已绑定
        if (isUserIdBound(userId)) {
            logger.info("用户工号: {} 已绑定，更新绑定关系", userId);
            Optional<UserBind> existingBind = userBindRepository.findByUserId(userId);
            if (existingBind.isPresent()) {
                UserBind userBind = existingBind.get();
                userBind.setOpenId(openId);
                return userBindRepository.save(userBind);
            }
        }
        
        // 检查微信openid是否已绑定
        if (isOpenIdBound(openId)) {
            logger.info("微信openid: {} 已绑定，更新绑定关系", openId);
            Optional<UserBind> existingBind = userBindRepository.findByOpenId(openId);
            if (existingBind.isPresent()) {
                UserBind userBind = existingBind.get();
                userBind.setUserId(userId);
                return userBindRepository.save(userBind);
            }
        }
        
        // 创建新的绑定关系
        logger.info("创建新的绑定关系: 用户工号: {}, 微信openid: {}", userId, openId);
        UserBind userBind = new UserBind();
        userBind.setUserId(userId);
        userBind.setOpenId(openId);
        return userBindRepository.save(userBind);
    }

    @Override
    public Optional<UserBind> findByUserId(String userId) {
        return userBindRepository.findByUserId(userId);
    }

    @Override
    public Optional<UserBind> findByOpenId(String openId) {
        return userBindRepository.findByOpenId(openId);
    }

    @Override
    public boolean isUserIdBound(String userId) {
        return userBindRepository.existsByUserId(userId);
    }

    @Override
    public boolean isOpenIdBound(String openId) {
        return userBindRepository.existsByOpenId(openId);
    }

    @Override
    public UserBind bindWithCaptcha(String userId, String openId, String captcha) {
        logger.info("开始验证码绑定: userId={}, openId={}", userId, openId);

        // 1. 验证验证码
        boolean verified = captchaService.verifyCaptcha(userId, captcha);
        if (!verified) {
            throw new RuntimeException("验证码错误");
        }

        // 2. 如果openId为空，尝试从缓存获取
        if (openId == null || openId.isEmpty()) {
            openId = captchaService.getStoredOpenId(userId);
            if (openId == null || openId.isEmpty()) {
                throw new RuntimeException("未找到openId，请重新发起绑定流程");
            }
        }

        // 3. 执行绑定
        UserBind result = bindUser(userId, openId);

        // 4. 清除验证码缓存
        captchaService.clearCaptcha(userId);

        logger.info("验证码绑定成功: userId={}, openId={}", userId, openId);
        return result;
    }
}