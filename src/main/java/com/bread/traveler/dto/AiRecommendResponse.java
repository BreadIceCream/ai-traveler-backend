package com.bread.traveler.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

/**
 * @author huang
 */
@Data
@Schema(description = "AI推荐响应")
@AllArgsConstructor
public class AiRecommendResponse {

    @Schema(description = "AI的回答", example = "[\"您好，推荐您去故宫博物院参观\", \"故宫是中国古代宫殿建筑群...\"]")
    private List<String> aiMessages;
    
    @Schema(description = "本次使用的工具名称", example = "[\"POI_SEARCH\", \"WEB_SEARCH\"]")
    private Set<String> toolUse;
    
    @Schema(description = "本次工具调用结果", example = "{\"POI_SEARCH\": [\"故宫博物院\", \"天安门广场\"], \"WEB_SEARCH\": [\"故宫开放时间\"]}")
    private Map<String, List<String>> toolCallResults;

}
