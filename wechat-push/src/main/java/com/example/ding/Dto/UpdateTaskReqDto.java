package com.example.ding.Dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateTaskReqDto {

    private String taskId;

    private String operatorId;

    private String subject;

    private String description;

    private List<String> executorIds;

    private String appkey;

    private String appsecret;

}
