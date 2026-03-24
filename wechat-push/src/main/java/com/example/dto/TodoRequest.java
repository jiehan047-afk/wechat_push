package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 待办/已办通知请求DTO
 */
@Data
@Schema(description = "待办/已办通知请求DTO")
public class TodoRequest {
    /**
     * 目标用户信息，格式：{"LoginName":"用户名"}
     */
    @Schema(description = "目标用户信息，格式：{\"LoginName\":\"用户名\"}")
    private String targets;

    /**
     * 通知类型：3-已办通知，为空-待办通知
     */
    @Schema(description = "通知类型：3-已办通知，为空-待办通知")
    private Integer type;

    /**
     * 页码
     */
    @Schema(description = "页码")
    private Integer pageNo;

    /**
     * 当前页条数
     */
    @Schema(description = "当前页条数")
    private Integer rowSize;
}