package com.example.repository;

import com.example.entity.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 待办事项Repository
 * 用于待办事项的数据库操作
 */
public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {
    
    /**
     * 根据用户ID和状态查询待办事项
     * @param userId 用户ID
     * @param status 状态：0-待办，1-已办，2-已忽略
     * @return 待办事项列表
     */
    List<TodoItem> findByUserIdAndStatusOrderByCreateTimeDesc(String userId, Integer status);
    
    /**
     * 根据用户ID和状态以及标题模糊查询待办事项
     * @param userId 用户ID
     * @param status 状态：0-待办，1-已办，2-已忽略
     * @param title 标题关键词
     * @return 待办事项列表
     */
    List<TodoItem> findByUserIdAndStatusAndTitleContainingOrderByCreateTimeDesc(String userId, Integer status, String title);
    
    /**
     * 查询用户待办事项数量
     * @param userId 用户ID
     * @return 待办事项数量
     */
    @Query("SELECT COUNT(t) FROM TodoItem t WHERE t.userId = :userId AND t.status = 0")
    Long countTodoItemsByUserId(@Param("userId") String userId);
}
