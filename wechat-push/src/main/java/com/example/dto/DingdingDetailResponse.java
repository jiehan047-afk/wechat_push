package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 钉钉详情响应DTO
 */
@Data
@Schema(description = "钉钉详情响应DTO")
public class DingdingDetailResponse {
    /**
     * 返回状态
     */
    @Schema(description = "返回状态")
    private Integer returnState;

    /**
     * 消息
     */
    @Schema(description = "消息")
    private String message;

    /**
     * 数据
     */
    @Schema(description = "数据")
    private DingdingDetailData data;

    /**
     * 钉钉详情数据
     */
    @Data
    @Schema(description = "钉钉详情数据")
    public static class DingdingDetailData {
        /**
         * 任务ID
         */
        @Schema(description = "任务ID")
        private String taskId;

        /**
         * 标题
         */
        @Schema(description = "标题")
        private String title;

        /**
         * 描述
         */
        @Schema(description = "描述")
        private String description;

        /**
         * 创建人ID
         */
        @Schema(description = "创建人ID")
        private String creatorId;

        /**
         * 创建人名称
         */
        @Schema(description = "创建人名称")
        private String creatorName;

        /**
         * 创建时间
         */
        @Schema(description = "创建时间")
        private String createTime;

        /**
         * 业务来源
         */
        @Schema(description = "业务来源")
        private String bizSource;

        /**
         * 业务ID
         */
        @Schema(description = "业务ID")
        private String bizId;

        /**
         * 详情URL
         */
        @Schema(description = "详情URL")
        private String detailUrl;

        /**
         * 接收人id
         */
        @Schema(description = "接收人id")
        private String recipientId;
    }
}
