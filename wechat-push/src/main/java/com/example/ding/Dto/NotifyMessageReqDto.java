package com.example.ding.Dto;

import lombok.Data;

import java.util.List;

@Data
public class NotifyMessageReqDto {

    private String creatorId;

    private String subject;

    private String description;

    private List<String> executorIds;

    private String appUrl;

    private String pcUrl;

    private Integer todoLevel;

    private String dingNotify;

    private String appName;

    private String appkey;

    private String appsecret;

    private Long agentId;
}
