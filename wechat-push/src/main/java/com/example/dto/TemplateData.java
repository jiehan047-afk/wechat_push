package com.example.dto;

import lombok.Data;

/**
 * 微信模板数据DTO
 * 用于微信模板消息的单个数据项
 */
@Data
public class TemplateData {
    /**
     * 数据值
     */
    private String value;
    
    /**
     * 数据颜色
     */
    private String color;
    
    /**
     * 默认构造函数
     */
    public TemplateData() {
    }
    
    /**
     * 构造函数，使用默认颜色
     * @param value 数据值
     */
    public TemplateData(String value) {
        this.value = value;
        this.color = "#173177";
    }
    
    /**
     * 构造函数，自定义颜色
     * @param value 数据值
     * @param color 数据颜色
     */
    public TemplateData(String value, String color) {
        this.value = value;
        this.color = color;
    }
}