package com.bread.traveler.tools;

import com.bread.traveler.constants.Constant;
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
import java.util.List;
import java.util.UUID;

/**
 * 智能推荐工具
 */
@Component
@Slf4j
public class RecommendationTools {

    public static final String TOOL_CONTEXT_CONVERSATION_ID = "conversationId";

    @Autowired
    private PoisService poisService;
    @Autowired
    private WebSearchService webSearchService;

    /**
     * 搜索网页工具：搜索网页，保存搜索到的WebPage
     * @param query
     * @param freshness
     * @param count
     * @param toolContext
     * @return
     * @throws IOException
     */
    @Tool(name = ToolNames.WEB_SEARCH_TOOL_NAME, description = "search for information from the web")
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
        return webSearchResults;
    }


    /**
     * POI搜索工具：搜索POI，保存搜索到的POI
     *
     * @param city
     * @param keyword
     * @return
     */
    @Tool(name = ToolNames.POI_SEARCH_TOOL_NAME, description = "Get information about a place or point of interest, including address, type, latitude and longitude, etc.")
    public List<Pois> poiSearch(
            @ToolParam(description = "The city where poi is located", required = false) String city,
            @ToolParam(description = "The poi keyword") String keyword){
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
        return pois;
    }
}