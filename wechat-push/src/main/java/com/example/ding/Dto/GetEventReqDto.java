package com.example.ding.Dto;

import lombok.Data;

@Data
public class GetEventReqDto {

    private String userId;

    private String calendarId;

    private String eventId;

    private String appkey;

    private String appsecret;

}
