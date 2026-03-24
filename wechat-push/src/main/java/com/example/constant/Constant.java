package com.example.constant;

/**
 * 常量类
 * 用于存放系统中所有的静态常量
 */
public class Constant {
    
    /**
     * 微信相关常量
     */
    public static class WeChat {
        // 微信API URL模板
        public static final String WX_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
        public static final String WX_SEND_TEMPLATE_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";
        public static final String WX_OAUTH2_AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=%s#wechat_redirect";
        public static final String WX_OAUTH2_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
        
        // Redis缓存键
        public static final String REDIS_KEY_ACCESS_TOKEN = "wechat:access_token";
        
        // 微信API返回码
        public static final String SUCCESS_CODE = "0";
        public static final String ERROR_CODE_KEY = "errcode";
        public static final String ACCESS_TOKEN_KEY = "access_token";
        public static final String EXPIRES_IN_KEY = "expires_in";
        public static final String OPENID_KEY = "openid";
    }
    
    /**
     * 钉钉相关常量
     */
    public static class DingTalk {
        // 钉钉API URL
        public static final String DING_USER_GETUSERINFO_URL = "https://oapi.dingtalk.com/topapi/v2/user/getuserinfo";
        public static final String DING_USER_GET_URL = "https://oapi.dingtalk.com/topapi/v2/user/get";
        
        // Redis缓存键模板
        public static final String REDIS_KEY_TOKEN_TEMPLATE = "ding:token:%s:%s";
        public static final String REDIS_KEY_USER_TEMPLATE = "ding:user:%s";
        public static final String REDIS_KEY_EVENT_TEMPLATE = "ding:event:%s";
        
        // 事件类型
        public static final String EVENT_TYPE_TODO_CHANGE = "todo_task_create";
    }

    /**
     * 验证码相关常量
     */
    public static class Captcha {
        // Redis缓存键模板
        public static final String REDIS_KEY_CAPTCHA_CODE = "captcha:code:%s";           // 验证码
        public static final String REDIS_KEY_CAPTCHA_LIMIT = "captcha:limit:%s";         // 发送频率限制
        public static final String REDIS_KEY_CAPTCHA_BIND = "captcha:bind:%s";           // 绑定信息暂存

        // 验证码配置
        public static final int CAPTCHA_LENGTH = 6;                // 验证码长度
        public static final int CAPTCHA_EXPIRE_MINUTES = 5;        // 验证码过期时间(分钟)
        public static final int CAPTCHA_LIMIT_SECONDS = 60;        // 发送间隔限制(秒)
    }
}
