package com.example.service;

import com.example.dto.TodoItemDTO;

import java.util.List;

/**
 * 待办事项服务接口
 * 定义待办事项的业务方法
 */
public interface TodoService {
    
    /**
     * 获取待办事项列表
     * @param userId 用户ID
     * @param title 标题关键词，可选
     * @return 待办事项列表
     */
    List<TodoItemDTO> getTodoList(String userId, String title);
    
    /**
     * 获取已办事项列表
     * @param userId 用户ID
     * @param title 标题关键词，可选
     * @return 已办事项列表
     */
    List<TodoItemDTO> getDoneList(String userId, String title);
    
    /**
     * 获取已忽略事项列表
     * @param userId 用户ID
     * @param title 标题关键词，可选
     * @return 已忽略事项列表
     */
    List<TodoItemDTO> getIgnoredList(String userId, String title);
    
    /**
     * 获取待办事项数量
     * @param userId 用户ID
     * @return 待办事项数量
     */
    Long getTodoCount(String userId);
    
    /**
     * 更新事项状态
     * @param id 事项ID
     * @param status 新状态：0-待办，1-已办，2-已忽略
     * @return 更新是否成功
     */
    boolean updateStatus(Long id, Integer status);
}
