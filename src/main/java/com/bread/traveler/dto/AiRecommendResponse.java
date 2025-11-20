package com.bread.traveler.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class AiRecommendResponse {

    // AI的回答
    private String aiMessage;
    // 本次使用的工具名称
    private List<String> toolUse;
    // 本次工具调用结果
    private Map<String, List<Object>> toolCallResults;

    public AiRecommendResponse() {
        this.toolUse = new ArrayList<>();
        this.toolCallResults = new HashMap<>();
    }

}
