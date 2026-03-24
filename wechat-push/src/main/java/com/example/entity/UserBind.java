package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 用户绑定实体类
 * 用于存储用户工号和微信openid的关联关系
 */
@Entity
@Table(name = "user_bind")
@Data
public class UserBind {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户工号
     */
    @Column(name = "user_id", unique = true, nullable = false, length = 50)
    private String userId;

    /**
     * 微信openid
     */
    @Column(name = "open_id", nullable = false, length = 100)
    private String openId;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @UpdateTimestamp
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;
}