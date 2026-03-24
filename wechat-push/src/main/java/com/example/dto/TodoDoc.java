package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 待办/已办通知文档DTO
 */
@Data
@Schema(description = "待办/已办通知文档DTO")
public class TodoDoc {
    /**
     * OA系统待办唯一标识，不为空
     */
    @Schema(description = "OA系统待办唯一标识，不为空")
    private String id;

    /**
     * 标题，不为空
     */
    @Schema(description = "标题，不为空")
    private String subject;

    /**
     * 待办类型，不为空
     */
    @Schema(description = "待办类型，不为空")
    private String type;

    /**
     * 待办关键字，可为空
     */
    @Schema(description = "待办关键字，可为空")
    private String key;

    /**
     * 参数1，可为空
     */
    @Schema(description = "参数1，可为空")
    private String param1;

    /**
     * 参数2，可为空
     */
    @Schema(description = "参数2，可为空")
    private String param2;

    /**
     * 待办来源，可为空，为空表示为OA系统待办
     */
    @Schema(description = "待办来源，可为空，为空表示为OA系统待办")
    private String appName;

    /**
     * 模块名，不为空
     */
    @Schema(description = "模块名，不为空")
    private String modelName;

    /**
     * 待办唯一标识，不为空。待办来源为空时，表示OA系统待办对应的主文档ID
     */
    @Schema(description = "待办唯一标识，不为空。待办来源为空时，表示OA系统待办对应的主文档ID")
    private String modelId;

    /**
     * 创建时间，不为空
     */
    @Schema(description = "创建时间，不为空")
    private String createTime;

    /**
     * 链接，不为空
     */
    @Schema(description = "链接，不为空")
    private String link;
}
