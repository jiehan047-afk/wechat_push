package com.example.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.dto.DingdingDetailResponse;
import com.example.dto.TodoResponse;
import com.example.service.SysNotifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统通知服务实现
 */
@Service
public class SysNotifyServiceImpl implements SysNotifyService {
    private static final Logger logger = LoggerFactory.getLogger(SysNotifyServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${sys-notify.api-url}")
    private String sysNotifyApiUrl;

    @Value("${sys-notify.basic-auth-token}")
    private String basicAuthToken;

    @Value("${sys-notify.basic-auth-token-detail}")
    private String basicAuthDetailToken;

    @Value("${sys-notify.dingding-detail-url}")
    private String dingdingDetailUrl;

    @Override
    public TodoResponse getTodo(String targets, Integer type, Integer rowSize, Integer pageNo) {
        logger.info("远程调用系统通知API: targets={}, type={}", targets, type);

        // 创建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + basicAuthToken);

        // 创建请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("targets", "{\"LoginName\":\"" + targets + "\"}");
        requestBody.put("type", type);
        requestBody.put("rowSize", rowSize);
        requestBody.put("pageNo", pageNo);

        // 创建请求实体
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            // 发送POST请求
            ResponseEntity<TodoResponse> responseEntity = restTemplate.exchange(
                    sysNotifyApiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    TodoResponse.class
            );

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                logger.info("远程调用成功");
                return JSON.parseObject(responseEntity.getBody().getMessage(), TodoResponse.class);
            } else {
                logger.error("远程调用失败，状态码: {}", responseEntity.getStatusCode());
                return createErrorResponse("远程调用失败");
            }
        } catch (Exception e) {
            logger.error("远程调用异常: {}", e.getMessage(), e);
            return createErrorResponse("远程调用异常: " + e.getMessage());
        }
    }

    /**
     * 创建错误响应
     * @param errorMessage 错误信息
     * @return 错误响应对象
     */
    private TodoResponse createErrorResponse(String errorMessage) {
        TodoResponse errorResponse = new TodoResponse();
        errorResponse.setErrorPage("true");
        errorResponse.setMessage(errorMessage);
        return errorResponse;
    }

    @Override
    public DingdingDetailResponse getDingdingDetail(String taskId) {
        logger.info("远程调用钉钉详情API: taskId={}", taskId);

        // 创建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + basicAuthDetailToken);

        // 创建请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("taskId", taskId);

        // 创建请求实体
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            // 发送POST请求
            ResponseEntity<DingdingDetailResponse> responseEntity = restTemplate.exchange(
                    dingdingDetailUrl,
                    HttpMethod.POST,
                    requestEntity,
                    DingdingDetailResponse.class
            );

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                logger.info("远程调用钉钉详情API成功");
                return responseEntity.getBody();
            } else {
                logger.error("远程调用钉钉详情API失败，状态码: {}", responseEntity.getStatusCode());
                return createDingdingDetailErrorResponse("远程调用失败");
            }
        } catch (Exception e) {
            logger.error("远程调用钉钉详情API异常: {}", e.getMessage(), e);
            return createDingdingDetailErrorResponse("远程调用异常: " + e.getMessage());
        }
    }

    /**
     * 创建钉钉详情错误响应
     * @param errorMessage 错误信息
     * @return 错误响应对象
     */
    private DingdingDetailResponse createDingdingDetailErrorResponse(String errorMessage) {
        DingdingDetailResponse errorResponse = new DingdingDetailResponse();
        errorResponse.setReturnState(0);
        errorResponse.setMessage(errorMessage);
        return errorResponse;
    }
}
