package com.example.ding.Dto;

import lombok.Data;

@Data
public class NotifyMessageRecallReqDto {

    private Long taskId;

    private Long agentId;

    private String appkey;

    private String appsecret;
}
