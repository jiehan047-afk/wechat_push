package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 待办事项DTO
 * 用于API响应
 */
@Data
@Schema(description = "待办事项DTO")
public class TodoItemDTO {
    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 标题
     */
    @Schema(description = "标题")
    private String title;

    /**
     * 发起人
     */
    @Schema(description = "发起人")
    private String initiator;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 系统来源
     */
    @Schema(description = "系统来源")
    private String systemSource;

    /**
     * 事项状态：0-待办，1-已办，2-已忽略
     */
    @Schema(description = "事项状态：0-待办，1-已办，2-已忽略")
    private Integer status;

    /**
     * 接收人
     */
    @Schema(description = "接收人")
    private String receiver;
}
