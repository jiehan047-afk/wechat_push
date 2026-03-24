package com.example.ding.Dto;

import lombok.Data;

import java.util.List;

@Data
public class BizResDto {

    private String eventId;
    private String syncAction;
    private String bizTag;
    private String taskId;
    private List<String> UnionIdList;

}


