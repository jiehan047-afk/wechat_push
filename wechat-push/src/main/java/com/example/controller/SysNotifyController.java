package com.example.controller;

import com.example.common.R;
import com.example.dto.*;
import com.example.service.SysNotifyService;
import com.example.service.WeChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    private WeChatService weChatService;

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


//    @PostMapping("/smartDingdingDetailRestService/sendMsg")
//    @Operation(summary = "获取钉钉详情", description = "根据任务ID获取钉钉详情")
//    public R<Void> getDingdingDetail(){
//
//
//        // 构建模板消息数据
//        Map<String, TemplateData> templateData = new HashMap<>();
//        templateData.put("thing8", new TemplateData("请审批【吴芳芳】提交的【研究生部部门，部长助理岗位】的人力需求申请".substring(0,16) +  "..."));
//        templateData.put("thing4", new TemplateData("Davishi", "#173177"));
//        templateData.put("time9", new TemplateData("2026-03-25", "#173177"));
//        // 调用服务层处理业务逻辑
//        boolean response = weChatService.sendTemplateMessage("o-sat2H23OJGrjLf-uPLkfssE_nc",templateData,"dasdjha");
//        System.out.println( response);
//        return R.success();
//    }
}
