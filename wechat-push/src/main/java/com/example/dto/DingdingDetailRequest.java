package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 钉钉详情请求DTO
 */
@Data
@Schema(description = "钉钉详情请求DTO")
public class DingdingDetailRequest {
    /**
     * 任务ID
     */
    @Schema(description = "任务ID", required = true)
    private String taskId;
}
