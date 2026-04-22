package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 用户账号实体类
 * 对应 user_accounts 表
 */
@Entity
@Table(name = "user_accounts")
@Data
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户名（工号）
     */
    @Column(name = "username", nullable = false, unique = true, length = 20)
    private String username;

    /**
     * 邮箱地址
     */
    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;

    /**
     * 记录创建时间
     */
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * 记录更新时间
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 是否激活：1=激活，0=禁用
     */
    @Column(name = "is_active")
    private Integer isActive;
}
