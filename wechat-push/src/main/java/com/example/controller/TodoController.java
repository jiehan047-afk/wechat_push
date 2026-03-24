package com.example.controller;

import com.example.dto.TodoItemDTO;
import com.example.service.TodoService;
import com.example.common.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 待办事项控制器
 * 用于处理待办事项的API请求
 */
@RestController
@RequestMapping("/api/todo")
@Tag(name = "待办事项", description = "待办事项管理API")
public class TodoController {
    
    private final TodoService todoService;
    
    /**
     * 构造函数注入
     * @param todoService 待办事项服务
     */
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }
    
    /**
     * 获取待办事项列表
     * @param userId 用户ID
     * @param title 标题关键词，可选
     * @return 待办事项列表
     */
    @GetMapping("/list/todo")
    @Operation(summary = "获取待办事项列表", description = "根据用户ID获取待办事项列表，支持标题搜索")
    public R<List<TodoItemDTO>> getTodoList(
            @Parameter(description = "用户ID", required = true) @RequestParam String userId,
            @Parameter(description = "标题关键词，可选") @RequestParam(required = false) String title) {
        List<TodoItemDTO> todoList = todoService.getTodoList(userId, title);
        return R.success(todoList);
    }
    
    /**
     * 获取已办事项列表
     * @param userId 用户ID
     * @param title 标题关键词，可选
     * @return 已办事项列表
     */
    @GetMapping("/list/done")
    @Operation(summary = "获取已办事项列表", description = "根据用户ID获取已办事项列表，支持标题搜索")
    public R<List<TodoItemDTO>> getDoneList(
            @Parameter(description = "用户ID", required = true) @RequestParam String userId,
            @Parameter(description = "标题关键词，可选") @RequestParam(required = false) String title) {
        List<TodoItemDTO> doneList = todoService.getDoneList(userId, title);
        return R.success(doneList);
    }
    
    /**
     * 获取已忽略事项列表
     * @param userId 用户ID
     * @param title 标题关键词，可选
     * @return 已忽略事项列表
     */
    @GetMapping("/list/ignored")
    @Operation(summary = "获取已忽略事项列表", description = "根据用户ID获取已忽略事项列表，支持标题搜索")
    public R<List<TodoItemDTO>> getIgnoredList(
            @Parameter(description = "用户ID", required = true) @RequestParam String userId,
            @Parameter(description = "标题关键词，可选") @RequestParam(required = false) String title) {
        List<TodoItemDTO> ignoredList = todoService.getIgnoredList(userId, title);
        return R.success(ignoredList);
    }
    
    /**
     * 获取待办事项数量
     * @param userId 用户ID
     * @return 待办事项数量
     */
    @GetMapping("/count/todo")
    @Operation(summary = "获取待办事项数量", description = "根据用户ID获取待办事项数量")
    public R<Long> getTodoCount(
            @Parameter(description = "用户ID", required = true) @RequestParam String userId) {
        Long count = todoService.getTodoCount(userId);
        return R.success(count);
    }
    
    /**
     * 更新事项状态
     * @param id 事项ID
     * @param status 新状态：0-待办，1-已办，2-已忽略
     * @return 更新结果
     */
    @PutMapping("/status/{id}")
    @Operation(summary = "更新事项状态", description = "更新待办事项的状态，0-待办，1-已办，2-已忽略")
    public R<Boolean> updateStatus(
            @Parameter(description = "事项ID", required = true) @PathVariable Long id,
            @Parameter(description = "新状态：0-待办，1-已办，2-已忽略", required = true) @RequestParam Integer status) {
        boolean result = todoService.updateStatus(id, status);
        return R.success(result);
    }
}
