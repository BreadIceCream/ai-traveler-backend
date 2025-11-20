package com.bread.traveler.utils;

import com.bread.traveler.constants.Constant;
import com.bread.traveler.dto.AiRecommendResponse;
import com.bread.traveler.service.PoisService;
import com.bread.traveler.service.WebSearchService;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class RecommendationTools {

    public static final String WEB_SEARCH_TOOL_NAME = "webSearch";
    public static final String POI_SEARCH_TOOL_NAME = "poiSearch";
    public static final String TOOL_CONTEXT_AI_RECOMMEND_RESPONSE = "aiRecommendResponse";
    public static final String TOOL_CONTEXT_CONVERSATION_ID = "conversationId";

    @Autowired
    private ActivityService activityService;
    @Autowired
    private PoisService poisService;
    @Autowired
    private WebSearchService webSearchService;

    /**
     * 搜索网页工具：搜索网页，保存搜索结果，并添加toolContext上下文信息
     * @param query
     * @param freshness
     * @param count
     * @param toolContext
     * @return
     * @throws IOException
     */
    @Tool(name = WEB_SEARCH_TOOL_NAME, description = "Web search")
    public WebSearchService.WebSearchResults webSearch(
            @ToolParam(description = "web search keywords") String query,
            @ToolParam(description = "web search time range", required = false) WebSearchService.Freshness freshness,
            @ToolParam(description = "web search result count", required = false) Integer count,
            ToolContext toolContext)
            throws IOException {
        // 构建参数
        WebSearchService.WebSearchParam param = WebSearchService.WebSearchParam.builder()
                .query(query)
                .summary(true)
                .freshness(freshness == null ? WebSearchService.Freshness.noLimit.name() : freshness.name())
                .count(count == null ? Constant.WEB_SEARCH_COUNT : count).build();
        // tool上下文，从中获取conversationId，调用搜索功能，将结果保存至数据库
        UUID conversationId = (UUID) toolContext.getContext().get(TOOL_CONTEXT_CONVERSATION_ID);
        WebSearchService.WebSearchResults webSearchResults = webSearchService.webSearch(conversationId, param);
        webSearchService.saveBatch(webSearchResults.getWebPages());
        // tool上下文，从中获取conversationId和AiRecommendResponse，添加工具调用信息和结果
        AiRecommendResponse response = (AiRecommendResponse) toolContext.getContext().get(TOOL_CONTEXT_AI_RECOMMEND_RESPONSE);
        // 添加工具调用信息
        List<String> toolUse = response.getToolUse();
        toolUse.add(WEB_SEARCH_TOOL_NAME);
        // 获取所有工具调用结果
        Map<String, List<Object>> toolCallResults = response.getToolCallResults();
        // 获取当前工具的历史调用结果，添加结果List<WebPage>
        List<Object> allResults = toolCallResults.getOrDefault(WEB_SEARCH_TOOL_NAME, new ArrayList<>());
        allResults.addAll(webSearchResults.getWebPages());
        toolCallResults.put(WEB_SEARCH_TOOL_NAME, allResults);
        response.setToolUse(toolUse);
        response.setToolCallResults(toolCallResults);
        return webSearchResults;
    }


}