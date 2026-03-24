package com.example.service;

import com.example.entity.UserBind;

import java.util.Optional;

/**
 * 用户绑定服务接口
 * 定义用户绑定的业务逻辑
 */
public interface UserBindService {
    
    /**
     * 绑定用户工号和微信openid
     * @param userId 用户工号
     * @param openId 微信openid
     * @return 绑定结果
     */
    UserBind bindUser(String userId, String openId);
    
    /**
     * 根据用户工号查询绑定关系
     * @param userId 用户工号
     * @return 绑定关系Optional
     */
    Optional<UserBind> findByUserId(String userId);
    
    /**
     * 根据微信openid查询绑定关系
     * @param openId 微信openid
     * @return 绑定关系Optional
     */
    Optional<UserBind> findByOpenId(String openId);
    
    /**
     * 判断用户工号是否已绑定
     * @param userId 用户工号
     * @return 是否已绑定
     */
    boolean isUserIdBound(String userId);
    
    /**
     * 判断微信openid是否已绑定
     * @param openId 微信openid
     * @return 是否已绑定
     */
    boolean isOpenIdBound(String openId);

    /**
     * 通过验证码绑定用户
     * @param userId 用户工号
     * @param openId 微信openid
     * @param captcha 验证码
     * @return 绑定结果
     */
    UserBind bindWithCaptcha(String userId, String openId, String captcha);
}