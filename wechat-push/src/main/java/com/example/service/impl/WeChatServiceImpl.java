package com.example.service.impl;

import com.example.constant.Constant;
import com.example.dto.TemplateData;
import com.example.service.WeChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 微信服务实现类
 * 负责处理微信授权和模板消息推送
 */
@Service
public class WeChatServiceImpl implements WeChatService {
    private static final Logger logger = LoggerFactory.getLogger(WeChatServiceImpl.class);
    
    @Value("${wechat.appid}")
    private String appId;
    
    @Value("${wechat.secret}")
    private String appSecret;
    
    @Value("${wechat.template-id}")
    private String templateId;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final StringRedisTemplate redisTemplate;
    
    /**
     * 构造函数注入StringRedisTemplate
     * @param redisTemplate Redis模板
     */
    public WeChatServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    /**
     * 获取微信全局access_token
     * 优先从Redis缓存获取，缓存不存在或已过期则调用微信API获取
     * @return access_token
     */
    @Override
    public String getAccessToken() {
        logger.info("开始获取微信access_token");
        
        // 1. 尝试从Redis缓存获取access_token
        String accessToken = redisTemplate.opsForValue().get(Constant.WeChat.REDIS_KEY_ACCESS_TOKEN);
        if (accessToken != null) {
            logger.info("从Redis缓存获取微信access_token成功: {}", accessToken);
            return accessToken;
        }
        
        // 2. 缓存不存在，调用微信API获取新的access_token
        logger.info("Redis缓存中无access_token，调用微信API获取");
        String url = String.format(Constant.WeChat.WX_ACCESS_TOKEN_URL, appId, appSecret);
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        
        if (response != null && response.containsKey(Constant.WeChat.ACCESS_TOKEN_KEY)) {
            accessToken = (String) response.get(Constant.WeChat.ACCESS_TOKEN_KEY);
            // 获取过期时间（微信返回的是秒数）
            Long expiresIn = response.containsKey(Constant.WeChat.EXPIRES_IN_KEY) ? ((Number) response.get(Constant.WeChat.EXPIRES_IN_KEY)).longValue() : 7200L;
            
            logger.info("获取微信access_token成功: {}，有效期: {}秒", accessToken, expiresIn);
            
            // 3. 将access_token存入Redis，设置过期时间（减去100秒防止缓存过期与实际过期时间不一致）
            redisTemplate.opsForValue().set(Constant.WeChat.REDIS_KEY_ACCESS_TOKEN, accessToken, expiresIn - 100, TimeUnit.SECONDS);
            logger.info("微信access_token已存入Redis缓存");
            
            return accessToken;
        } else {
            logger.error("获取微信access_token失败: {}", response);
            throw new RuntimeException("获取微信access_token失败");
        }
    }
    
    /**
     * 发送微信模板消息
     * @param openId 微信openid
     * @param templateData 模板数据
     * @return 发送结果
     */
    @Override
    public boolean sendTemplateMessage(String openId, Map<String, TemplateData> templateData) {
        logger.info("开始发送微信模板消息给openid: {}", openId);
        
        try {
            String accessToken = getAccessToken();
            String url = String.format(Constant.WeChat.WX_SEND_TEMPLATE_MESSAGE_URL, accessToken);
            
            Map<String, Object> requestBody = Map.of(
                "touser", openId,
                "template_id", templateId,
                "data", templateData
            );
            
            Map<String, Object> response = restTemplate.postForObject(url, requestBody, Map.class);
            
            if (response != null && Constant.WeChat.SUCCESS_CODE.equals(response.get(Constant.WeChat.ERROR_CODE_KEY))) {
                logger.info("发送微信模板消息成功");
                return true;
            } else {
                logger.error("发送微信模板消息失败: {}", response);
                return false;
            }
        } catch (Exception e) {
            logger.error("发送微信模板消息异常: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 生成微信授权URL
     * @param redirectUri 回调URL
     * @param state 状态参数
     * @return 授权URL
     */
    @Override
    public String generateOAuth2Url(String redirectUri, String state) {
        logger.info("生成微信授权URL，回调地址: {}", redirectUri);
        return String.format(Constant.WeChat.WX_OAUTH2_AUTHORIZE_URL, appId, redirectUri, state);
    }
    
    /**
     * 根据授权code获取openid
     * @param code 授权code
     * @return openid
     */
    @Override
    public String getOpenIdByCode(String code) {
        logger.info("开始根据code获取openid: {}", code);
        String url = String.format(Constant.WeChat.WX_OAUTH2_ACCESS_TOKEN_URL, appId, appSecret, code);
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        
        if (response != null && response.containsKey(Constant.WeChat.OPENID_KEY)) {
            String openId = (String) response.get(Constant.WeChat.OPENID_KEY);
            logger.info("获取openid成功: {}", openId);
            return openId;
        } else {
            logger.error("获取openid失败: {}", response);
            throw new RuntimeException("获取openid失败");
        }
    }
}