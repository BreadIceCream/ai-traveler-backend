package com.bread.traveler.tools;

import com.bread.traveler.constants.Constant;
import com.bread.traveler.dto.AiRecommendResponse;
import com.bread.traveler.entity.Pois;
import com.bread.traveler.service.PoisService;
import com.bread.traveler.service.WebSearchService;
import lombok.extern.slf4j.Slf4j;
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

/**
 * 智能推荐工具
 */
@Component
@Slf4j
public class RecommendationTools {

    public static final String TOOL_CONTEXT_AI_RECOMMEND_RESPONSE = "aiRecommendResponse";
    public static final String TOOL_CONTEXT_CONVERSATION_ID = "conversationId";

    @Autowired
    private PoisService poisService;
    @Autowired
    private WebSearchService webSearchService;

    /**
     * 搜索网页工具：搜索网页，保存搜索到的WebPage，并添加toolContext上下文信息
     * @param query
     * @param freshness
     * @param count
     * @param toolContext
     * @return
     * @throws IOException
     */
    @Tool(name = ToolNames.WEB_SEARCH_TOOL_NAME, description = "Web search")
    public WebSearchService.WebSearchResults webSearchAndSave(
            @ToolParam(description = "web search keywords") String query,
            @ToolParam(description = "web search time range", required = false) WebSearchService.Freshness freshness,
            @ToolParam(description = "web search result count", required = false) Integer count,
            // tool上下文，需要conversationId
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
        handleToolContext(toolContext, ToolNames.WEB_SEARCH_TOOL_NAME, webSearchResults.getWebPages());
        return webSearchResults;
    }


    /**
     * POI搜索工具：搜索POI，保存搜索到的POI，并添加toolContext上下文信息
     * @param city
     * @param keyword
     * @param toolContext
     * @return
     */
    @Tool(name = ToolNames.POI_SEARCH_TOOL_NAME, description = "POI search")
    public List<Pois> poiSearch(
            @ToolParam(description = "The city where poi is located", required = false) String city,
            @ToolParam(description = "The poi keyword") String keyword,
            // tool上下文，需要conversationId和aiRecommendResponse
            ToolContext toolContext){
        // 先从数据库中搜索
        List<Pois> pois = null;
        try {
            pois = poisService.searchPoiFromDb(city, keyword);
        } catch (Exception e) {
            log.error("TOOL {}: search poi from db error", ToolNames.POI_SEARCH_TOOL_NAME, e);
        }
        if (pois == null || pois.isEmpty()){
            // DB搜索为空，则调用第三方API搜索
            log.info("TOOL {}: search poi from external api", ToolNames.POI_SEARCH_TOOL_NAME);
            pois = poisService.searchPoiFromExternalApiAndSaveUpdate(city, keyword);
        }
        handleToolContext(toolContext, ToolNames.POI_SEARCH_TOOL_NAME, pois);
        return pois;
    }

    //todo配置高德mcp工具

    private void handleToolContext(ToolContext toolContext, String toolName, List<?> toolExecutionResults){
        // tool上下文，添加工具调用信息和结果
        AiRecommendResponse response = (AiRecommendResponse) toolContext.getContext().get(TOOL_CONTEXT_AI_RECOMMEND_RESPONSE);
        // 添加工具调用信息
        List<String> toolUse = response.getToolUse();
        toolUse.add(toolName);
        // 获取所有工具调用结果
        Map<String, List<Object>> allToolResults = response.getToolCallResults();
        // 获取当前工具的历史调用结果，添加结果
        List<Object> currentToolResults = allToolResults.getOrDefault(toolName, new ArrayList<>());
        currentToolResults.addAll(toolExecutionResults);
        allToolResults.put(toolName, currentToolResults);
        // 设置response
        response.setToolUse(toolUse);
        response.setToolCallResults(allToolResults);
    }
}