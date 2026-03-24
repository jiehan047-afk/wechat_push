package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 待办/已办通知响应DTO
 */
@Data
@Schema(description = "待办/已办通知响应DTO")
public class TodoResponse {
    /**
     * 总页数
     */
    @Schema(description = "总页数")
    private String pageCount;

    /**
     * 当前页码
     */
    @Schema(description = "当前页码")
    private String pageno;

    /**
     * 文档总数
     */
    @Schema(description = "文档总数")
    private String count;

    /**
     * 文档列表
     */
    @Schema(description = "文档列表")
    private List<TodoDoc> docs;

    /**
     * 请求数据错误标志
     */
    @Schema(description = "请求数据错误标志")
    private String errorPage;

    /**
     * 错误信息
     */
    @Schema(description = "错误信息")
    private String message;
}
