package com.example.service;

import com.example.dto.TemplateData;

import java.util.Map;

/**
 * 微信服务接口
 * 定义微信授权和模板消息推送的业务方法
 */
public interface WeChatService {
    
    /**
     * 获取微信全局access_token
     * @return access_token
     */
    String getAccessToken();
    
    /**
     * 发送微信模板消息
     * @param openId 微信openid
     * @param templateData 模板数据
     * @return 发送结果
     */
    boolean sendTemplateMessage(String openId, Map<String, TemplateData> templateData);
    
    /**
     * 生成微信授权URL
     * @param redirectUri 回调URL
     * @param state 状态参数
     * @return 授权URL
     */
    String generateOAuth2Url(String redirectUri, String state);
    
    /**
     * 根据授权code获取openid
     * @param code 授权code
     * @return openid
     */
    String getOpenIdByCode(String code);
}