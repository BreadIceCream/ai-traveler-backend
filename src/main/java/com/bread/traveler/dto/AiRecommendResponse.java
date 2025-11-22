package com.bread.traveler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

@Data
@AllArgsConstructor
public class AiRecommendResponse {

    // AI的回答
    private List<String> aiMessages;
    // 本次使用的工具名称
    private Set<String> toolUse;
    // 本次工具调用结果
    private Map<String, List<String>> toolCallResults;

}
