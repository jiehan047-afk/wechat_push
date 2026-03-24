package com.example.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.open.app.api.GenericEventListener;
import com.dingtalk.open.app.api.OpenDingTalkStreamClientBuilder;
import com.dingtalk.open.app.api.message.GenericOpenDingTalkEvent;
import com.dingtalk.open.app.api.security.AuthClientCredential;
import com.dingtalk.open.app.stream.protocol.event.EventAckStatus;
import com.example.constant.Constant;
import com.example.ding.Dto.BizResDto;
import com.example.dto.DingdingDetailResponse;
import com.example.dto.TemplateData;
import com.example.entity.UserBind;
import com.example.service.SysNotifyService;
import com.example.service.UserBindService;
import com.example.service.WeChatService;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 钉钉事件监听器服务实现类
 * 负责处理钉钉事件的监听和处理逻辑
 */
@Service
public class DingTalkEventListenerServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(DingTalkEventListenerServiceImpl.class);

    @Value("${dingtalk.client-id}")
    private String clientId;

    @Value("${dingtalk.client-secret}")
    private String clientSecret;
    
    @Value("${dingtalk.duration}")
    private long duration;
    
    // 线程池配置
    @Value("${thread-pool.core-pool-size}")
    private int corePoolSize;
    
    @Value("${thread-pool.max-pool-size}")
    private int maxPoolSize;
    
    @Value("${thread-pool.queue-capacity}")
    private int queueCapacity;
    
    @Value("${thread-pool.keep-alive-seconds}")
    private int keepAliveSeconds;
    
    @Value("${thread-pool.thread-name-prefix}")
    private String threadNamePrefix;
    
    // 自定义线程池
    private java.util.concurrent.ExecutorService executorService;
    
    @Autowired
    private UserBindService userBindService;
    
    @Autowired
    private WeChatService weChatService;
    
    @Autowired
    private SysNotifyService sysNotifyService;
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 初始化线程池
     */
    @PostConstruct
    public void initThreadPool() {
        logger.info("初始化线程池，corePoolSize: {}, maxPoolSize: {}, queueCapacity: {}", 
                corePoolSize, maxPoolSize, queueCapacity);
        executorService = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveSeconds,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(queueCapacity),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName(threadNamePrefix + thread.getId());
                        return thread;
                    }
                },
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    /**
     * 注册待办监听事件
     */
    @PostConstruct
    public void registerAllEventListener() throws Exception {
        OpenDingTalkStreamClientBuilder
                .custom()
                .credential(new AuthClientCredential(clientId, clientSecret))
                //注册事件监听
                .registerAllEventListener(new GenericEventListener() {
                    public EventAckStatus onEvent(GenericOpenDingTalkEvent event) {
                        logger.info("待办回调事件日志：{}", event.toString());
                        try {
                            //事件唯一Id
                            String eventId = event.getEventId();
                            //事件类型
                            String eventType = event.getEventType();
                            //获取事件体
                            Object data = event.getData();
                            // 转换为JSONObject
                            JSONObject bizData = JSONObject.parseObject(JSONObject.toJSONString(data));
                            //处理事件
                            process(bizData, eventType, eventId);
                            //消费成功
                            return EventAckStatus.SUCCESS;
                        } catch (Exception e) {
                            logger.error("消费失败event:{},消费失败原因：{}", event.toString(), e);
                            //消费失败
                            return EventAckStatus.LATER;
                        }
                    }
                })
                .build().start();
    }

    void process(JSONObject bizData, String eventType, String eventId) throws Exception {
        String redisKey = MessageFormat.format(Constant.DingTalk.REDIS_KEY_EVENT_TEMPLATE, eventId);
        // 先从 Redis 中获取待办信息
        String cachedInfo = redisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isBlank(cachedInfo)) {
            logger.info("redis中缓存已存在eventId:{}，无需处理!", eventId);
            return;
        }

        if (Objects.equals(eventType, Constant.DingTalk.EVENT_TYPE_TODO_CHANGE)) {
            logger.info("接收到待办变更事件:{}", bizData.toJSONString());
            // 简化处理，实际项目中需要根据具体的事件结构解析
            BizResDto bizResDto = JSON.parseObject(JSON.toJSONString(bizData), BizResDto.class);
            String taskId = bizResDto.getTaskId();
            if (taskId == null) {
                logger.info("接收到待办变更事件为空");
                return;
            }
            // 使用线程池异步处理事件
            executorService.execute(() -> {
                try {
                    logger.info("线程池开始处理事件，taskId: {}, eventId: {}", taskId, eventId);
                    // 调用钉钉详情接口获取待办信息
                    DingdingDetailResponse detailResponse = sysNotifyService.getDingdingDetail(taskId);
                    logger.info("获取钉钉详情成功: {}", detailResponse);
                    
                    // 发送微信通知给相关用户
                    sendWeChatNotification(detailResponse);
                    
                    // 这里可以添加其他后续处理逻辑，比如：
                    // 1. 保存待办信息到数据库
                    // 2. 其他业务逻辑处理
                    
                } catch (Exception e) {
                    logger.error("获取钉钉详情失败，taskId: {}", taskId, e);
                } finally {
                    // 写入缓存
                    try {
                        redisTemplate.opsForValue().set(redisKey, redisKey, Duration.ofSeconds(duration));
                        logger.info("事件处理完成，已写入缓存，eventId: {}", eventId);
                    } catch (Exception e) {
                        logger.error("写入缓存失败，eventId: {}", eventId, e);
                    }
                }
            });
        }
    }

    /**
     * 发送微信通知给相关用户
     * @param detailResponse 钉钉详情响应
     */
    private void sendWeChatNotification(DingdingDetailResponse detailResponse) {
        try {
            if (detailResponse == null || detailResponse.getData() == null) {
                logger.warn("钉钉详情为空，无法发送微信通知");
                return;
            }
            
            DingdingDetailResponse.DingdingDetailData data = detailResponse.getData();
            // 假设creatorId是用户工号，实际项目中可能需要从事件数据中获取接收人信息
            String userId = data.getCreatorId();
            if (userId == null) {
                logger.warn("用户ID为空，无法发送微信通知");
                return;
            }
            
            // 查询用户绑定关系，获取微信openid
            Optional<UserBind> userBindOptional = userBindService.findByUserId(userId);
            if (userBindOptional.isEmpty()) {
                logger.warn("用户{}未绑定微信，无法发送微信通知", userId);
                return;
            }
            
            String openId = userBindOptional.get().getOpenId();
            logger.info("用户{}的微信openid为: {}", userId, openId);
            
            // 构建模板消息数据
            Map<String, TemplateData> templateData = new HashMap<>();
            templateData.put("thing11", new TemplateData(data.getTitle(), "#173177"));
            templateData.put("thing9", new TemplateData(data.getDescription(), "#173177"));
            templateData.put("time3", new TemplateData(data.getCreateTime(), "#173177"));

            // 发送微信模板消息
            boolean result = weChatService.sendTemplateMessage(openId, templateData);
            if (result) {
                logger.info("微信通知发送成功，用户: {}", userId);
            } else {
                logger.error("微信通知发送失败，用户: {}", userId);
            }
            
        } catch (Exception e) {
            logger.error("发送微信通知失败", e);
        }
    }


}