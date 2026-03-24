package com.example.controller;

import com.example.common.R;
import com.example.dto.DingdingDetailRequest;
import com.example.dto.DingdingDetailResponse;
import com.example.dto.TodoRequest;
import com.example.dto.TodoResponse;
import com.example.service.SysNotifyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 系统通知控制器
 * 用于处理系统通知相关的API请求
 */
@RestController
@RequestMapping("/api/sys-notify")
@Tag(name = "系统通知", description = "系统通知相关的API接口")
public class SysNotifyController {
    private static final Logger logger = LoggerFactory.getLogger(SysNotifyController.class);

    @Autowired
    private SysNotifyService sysNotifyService;

    /**
     * 获取待办/已办通知列表
     * @param request 请求参数
     * @return 通知列表
     */
    @PostMapping("/sysNotifyTodoRestService/getTodo")
    @Operation(summary = "获取通知列表", description = "获取待办或已办通知列表，type=3表示已办，为空表示待办")
    public R<TodoResponse> getTodo(
            @Parameter(description = "请求参数", required = true) @RequestBody TodoRequest request) {
        logger.info("获取通知列表请求: targets={}, type={}", request.getTargets(), request.getType());
        
        // 调用服务层处理业务逻辑
        TodoResponse response = sysNotifyService.getTodo(request.getTargets(), request.getType(), request.getRowSize(), request.getPageNo());
        
        return R.success(response);
    }

    /**
     * 获取钉钉详情
     * @param request 请求参数
     * @return 钉钉详情
     */
    @PostMapping("/smartDingdingDetailRestService/dingdingDetail")
    @Operation(summary = "获取钉钉详情", description = "根据任务ID获取钉钉详情")
    public R<DingdingDetailResponse> getDingdingDetail(
            @Parameter(description = "请求参数", required = true) @RequestBody DingdingDetailRequest request) {
        logger.info("获取钉钉详情请求: taskId={}", request.getTaskId());
        
        // 调用服务层处理业务逻辑
        DingdingDetailResponse response = sysNotifyService.getDingdingDetail(request.getTaskId());
        
        return R.success(response);
    }
}
