package com.example.ding;

import com.aliyun.dingtalkoauth2_1_0.Client;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenResponse;
import com.aliyun.teaopenapi.models.Config;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.request.OapiV2UserGetuserinfoRequest;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.dingtalk.api.response.OapiV2UserGetuserinfoResponse;
import com.alibaba.fastjson.JSON;
import com.example.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class DingApiClient {

    @Value("${dingtalk.client-id}")
    private String appkey;
    @Value("${dingtalk.client-secret}")
    private String appsecret;
    @Value("${dingtalk.duration}")
    private long duration;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 获取企业内部应用的access_token（
     * <a href="https://open.dingtalk.com/document/orgapp/obtain-the-access_token-of-an-internal-app">...</a>）
     *
     * @param appkey
     * @param appsecret
     * @return
     * @throws Exception
     */
    public String getToken(String appkey, String appsecret) throws Exception {
        // 生成 Redis 缓存 Key
        String redisKey = MessageFormat.format(Constant.DingTalk.REDIS_KEY_TOKEN_TEMPLATE, appkey, appsecret);
        
        // 先尝试从缓存获取
        String cachedToken = redisTemplate.opsForValue().get(redisKey);
        if (cachedToken != null) {
            log.info("从 Redis 缓存中获取 token 成功");
            return cachedToken;
        }

        // 无缓存时调用远程接口
        GetAccessTokenResponse response;
        try {
            Config config = new Config();
            Client client = new Client(config);
            GetAccessTokenRequest request = new GetAccessTokenRequest()
                .setAppKey(appkey)
                .setAppSecret(appsecret);
            response = client.getAccessToken(request);
            log.info("获取token成功: {}", JSON.toJSONString(response));
        } catch (Exception e) {
            log.error("获取token失败", e);
            throw new Exception("获取钉钉token失败: " + e.getMessage());
        }

        // 写入缓存
        String token = response.getBody().getAccessToken();
        redisTemplate.opsForValue().set(redisKey, token, Duration.ofSeconds(duration));
        return token;
    }

    /**
     * 通过免登码获取用户信息（
     * <a href="https://open.dingtalk.com/document/orgapp/obtain-the-userid-of-a-user-by-using-the-log-free">...</a>）
     *
     * @param code
     * @return
     * @throws Exception
     */
    public OapiV2UserGetuserinfoResponse.UserGetByCodeResponse getUserId(String code) throws Exception {
        log.info("根据code获取userinfo开始:{}", code);
        DingTalkClient client = new DefaultDingTalkClient(Constant.DingTalk.DING_USER_GETUSERINFO_URL);
        OapiV2UserGetuserinfoRequest req = new OapiV2UserGetuserinfoRequest();
        req.setCode(code);
        OapiV2UserGetuserinfoResponse rsp;
        try {
            rsp = client.execute(req, getToken(appkey, appsecret));
            log.info("根据code获取userinfo成功:{}", JSON.toJSONString(rsp));
        } catch (Exception e) {
            log.error("根据code获取userinfo失败:", e);
            throw new Exception("根据code获取用户信息失败: " + e.getMessage());
        }
        return rsp.getResult();
    }

    public OapiV2UserGetResponse.UserGetResponse getUserInfo(String userId, String appkey, String appsecret) throws Exception {
        log.info("根据userId获取userinfo开始:{}", userId);
        // 生成 Redis 缓存的 key
        String redisKey = MessageFormat.format(Constant.DingTalk.REDIS_KEY_USER_TEMPLATE, userId);
        
        // 先从 Redis 中获取用户信息
        String cachedUserInfo = redisTemplate.opsForValue().get(redisKey);
        if (cachedUserInfo != null) {
            return JSON.parseObject(cachedUserInfo, OapiV2UserGetResponse.UserGetResponse.class);
        }

        OapiV2UserGetResponse rsp = getDingUserInfo(userId, appkey, appsecret);
        return rsp.getResult();
    }

    public OapiV2UserGetResponse getDingUserInfo(String userId, String appkey, String appsecret) throws Exception {
        // 生成 Redis 缓存的 key
        String redisKey = MessageFormat.format(Constant.DingTalk.REDIS_KEY_USER_TEMPLATE, userId);
        OapiV2UserGetResponse rsp = getOapiV2UserGetResponse(userId, appkey, appsecret);
        if (!rsp.isSuccess()) {
            log.error("根据userId获取userinfo失败:{}", rsp.getErrmsg());
            throw new Exception("根据userId获取用户信息失败: " + rsp.getErrmsg());
        }
        try {
            // 将用户信息存入 Redis 缓存
            redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(rsp.getResult()), Duration.ofSeconds(duration));
        } catch (Exception e) {
            log.error("新增用户信息:{},失败:{}", JSON.toJSONString(rsp.getResult()), e);
        }
        return rsp;
    }

    private OapiV2UserGetResponse getOapiV2UserGetResponse(String userId, String appkey, String appsecret) throws Exception {
        OapiV2UserGetResponse rsp;
        try {
            DingTalkClient client = new DefaultDingTalkClient(Constant.DingTalk.DING_USER_GET_URL);
            OapiV2UserGetRequest req = new OapiV2UserGetRequest();
            req.setUserid(userId);
            req.setLanguage("zh_CN");
            rsp = client.execute(req, getToken(appkey, appsecret));
            log.info("根据userId获取userinfo成功:{}", JSON.toJSONString(rsp));
        } catch (Exception e) {
            log.error("根据userId获取userinfo失败:", e);
            throw new Exception("根据userId获取用户信息失败: " + e.getMessage());
        }
        return rsp;
    }

}
