package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 待办事项实体类
 * 用于存储待办、已办和已忽略的事项
 */
@Entity
@Table(name = "todo_item")
@Data
public class TodoItem {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 标题
     */
    @Column(name = "title", nullable = false, length = 255)
    private String title;

    /**
     * 发起人
     */
    @Column(name = "initiator", length = 50)
    private String initiator;

    /**
     * 创建时间
     */
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    /**
     * 系统来源
     */
    @Column(name = "system_source", length = 100)
    private String systemSource;

    /**
     * 事项状态：0-待办，1-已办，2-已忽略
     */
    @Column(name = "status", nullable = false)
    private Integer status;

    /**
     * 关联的用户ID
     */
    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    /**
     * 接收人
     */
    @Column(name = "receiver", length = 50)
    private String receiver;

    /**
     * 更新时间
     */
    @UpdateTimestamp
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;
}
