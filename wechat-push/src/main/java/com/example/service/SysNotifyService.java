package com.example.service;

import com.example.dto.DingdingDetailResponse;
import com.example.dto.TodoResponse;

/**
 * 系统通知服务接口
 */
public interface SysNotifyService {
    /**
     * 远程调用获取通知列表
     * @param targets 目标用户信息，格式：{"LoginName":"用户名"}
     * @param type 通知类型：3-已办通知，为空-待办通知
     * @return 通知列表响应
     */
    TodoResponse getTodo(String targets, Integer type, Integer rowSize, Integer pageNo);

    /**
     * 远程调用获取钉钉详情
     * @param taskId 任务ID
     * @return 钉钉详情响应
     */
    DingdingDetailResponse getDingdingDetail(String taskId);
}
