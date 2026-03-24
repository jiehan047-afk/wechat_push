package com.example.service.impl;

import com.example.dto.TodoItemDTO;
import com.example.entity.TodoItem;
import com.example.repository.TodoItemRepository;
import com.example.service.TodoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 待办事项服务实现类
 * 实现待办事项的业务逻辑
 */
@Service
public class TodoServiceImpl implements TodoService {
    private static final Logger logger = LoggerFactory.getLogger(TodoServiceImpl.class);
    
    // 事项状态常量
    private static final Integer STATUS_TODO = 0;
    private static final Integer STATUS_DONE = 1;
    private static final Integer STATUS_IGNORED = 2;
    
    private final TodoItemRepository todoItemRepository;
    
    /**
     * 构造函数注入
     * @param todoItemRepository 待办事项Repository
     */
    public TodoServiceImpl(TodoItemRepository todoItemRepository) {
        this.todoItemRepository = todoItemRepository;
    }
    
    @Override
    public List<TodoItemDTO> getTodoList(String userId, String title) {
        logger.info("获取待办事项列表，用户ID: {}, 标题关键词: {}", userId, title);
        
        List<TodoItem> todoItems;
        if (title != null && !title.isEmpty()) {
            todoItems = todoItemRepository.findByUserIdAndStatusAndTitleContainingOrderByCreateTimeDesc(userId, STATUS_TODO, title);
        } else {
            todoItems = todoItemRepository.findByUserIdAndStatusOrderByCreateTimeDesc(userId, STATUS_TODO);
        }
        
        return convertToDTOList(todoItems);
    }
    
    @Override
    public List<TodoItemDTO> getDoneList(String userId, String title) {
        logger.info("获取已办事项列表，用户ID: {}, 标题关键词: {}", userId, title);
        
        List<TodoItem> todoItems;
        if (title != null && !title.isEmpty()) {
            todoItems = todoItemRepository.findByUserIdAndStatusAndTitleContainingOrderByCreateTimeDesc(userId, STATUS_DONE, title);
        } else {
            todoItems = todoItemRepository.findByUserIdAndStatusOrderByCreateTimeDesc(userId, STATUS_DONE);
        }
        
        return convertToDTOList(todoItems);
    }
    
    @Override
    public List<TodoItemDTO> getIgnoredList(String userId, String title) {
        logger.info("获取已忽略事项列表，用户ID: {}, 标题关键词: {}", userId, title);
        
        List<TodoItem> todoItems;
        if (title != null && !title.isEmpty()) {
            todoItems = todoItemRepository.findByUserIdAndStatusAndTitleContainingOrderByCreateTimeDesc(userId, STATUS_IGNORED, title);
        } else {
            todoItems = todoItemRepository.findByUserIdAndStatusOrderByCreateTimeDesc(userId, STATUS_IGNORED);
        }
        
        return convertToDTOList(todoItems);
    }
    
    @Override
    public Long getTodoCount(String userId) {
        logger.info("获取待办事项数量，用户ID: {}", userId);
        return todoItemRepository.countTodoItemsByUserId(userId);
    }
    
    @Override
    public boolean updateStatus(Long id, Integer status) {
        logger.info("更新事项状态，ID: {}, 新状态: {}", id, status);
        
        try {
            TodoItem todoItem = todoItemRepository.findById(id).orElse(null);
            if (todoItem != null) {
                todoItem.setStatus(status);
                todoItemRepository.save(todoItem);
                logger.info("更新事项状态成功");
                return true;
            } else {
                logger.error("更新事项状态失败：事项不存在");
                return false;
            }
        } catch (Exception e) {
            logger.error("更新事项状态异常: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 将TodoItem列表转换为TodoItemDTO列表
     * @param todoItems TodoItem列表
     * @return TodoItemDTO列表
     */
    private List<TodoItemDTO> convertToDTOList(List<TodoItem> todoItems) {
        return todoItems.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 将TodoItem转换为TodoItemDTO
     * @param todoItem TodoItem
     * @return TodoItemDTO
     */
    private TodoItemDTO convertToDTO(TodoItem todoItem) {
        TodoItemDTO dto = new TodoItemDTO();
        dto.setId(todoItem.getId());
        dto.setTitle(todoItem.getTitle());
        dto.setInitiator(todoItem.getInitiator());
        dto.setCreateTime(todoItem.getCreateTime());
        dto.setSystemSource(todoItem.getSystemSource());
        dto.setStatus(todoItem.getStatus());
        dto.setReceiver(todoItem.getReceiver());
        return dto;
    }
}
