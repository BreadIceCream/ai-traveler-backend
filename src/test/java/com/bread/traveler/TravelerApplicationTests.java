package com.bread.traveler;

import cn.hutool.json.JSONObject;
import com.bread.traveler.constants.Constant;
import com.bread.traveler.dto.AiRecommendResponse;
import com.bread.traveler.entity.NonPoiItem;
import com.bread.traveler.entity.Users;
import com.bread.traveler.entity.WebPage;
import com.bread.traveler.enums.NonPoiType;
import com.bread.traveler.service.NonPoiItemService;
import com.bread.traveler.service.UsersService;
import com.bread.traveler.service.WebSearchService;
import com.bread.traveler.utils.GaoDeUtils;
import com.bread.traveler.tools.RecommendationTools;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
class TravelerApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private UsersService usersService;
    @Autowired
    private EmbeddingModel embeddingModel;
    @Autowired
    private GaoDeUtils gaoDeUtils;
    @Autowired
    private WebSearchService webSearchService;
    @Autowired
    private RecommendationTools recommendationTools;
    @Autowired
    private NonPoiItemService nonPoiItemService;
    @Autowired
    private List<McpSyncClient> mcpSyncClients;

    @Test
    void testEmbedding() {
        List<Users> users = usersService.list();
        for (Users user : users) {
            usersService.updateUserPreferences(user.getUserId(), user.getPreferencesText());
        }
    }

    @Test
    void testPOISearch() throws IOException {
        JSONObject results = gaoDeUtils.searchPoi(GaoDeUtils.SearchPoiParam.builder()
                .keywords("北京大学")
                .showFields(List.of(Constant.SHOW_FIELD.BUSINESS, Constant.SHOW_FIELD.PHOTOS))
                .pageSize(5)
                .build());
        System.out.println(results);
    }

    @Test
    void testWebSearch() {
        try {
            WebSearchService.WebSearchResults results = webSearchService.webSearch(
                    UUID.randomUUID(),
                    WebSearchService.WebSearchParam.builder()
                    .query("北京大学").summary(true).build());
            System.out.println(results);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testWebSearchTool(){
        AiRecommendResponse response = new AiRecommendResponse();
        ToolContext toolContext = new ToolContext(
                Map.of(RecommendationTools.TOOL_CONTEXT_CONVERSATION_ID, UUID.randomUUID(),
                        RecommendationTools.TOOL_CONTEXT_AI_RECOMMEND_RESPONSE, response));
        try {
            WebSearchService.WebSearchResults results = recommendationTools.webSearchAndSave("北京旅游攻略", null, 1, toolContext);
            System.out.println(results.getOriginalQuery());
            System.out.println(results.getResultCount());
            System.out.println(results.getWebPages());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testExtract(){
        Users bread = usersService.getUserByUsername("Bread");
        WebPage webPage = webSearchService.getById(UUID.fromString("9f86e6b3-182d-488f-a493-37fb31c69aee"));
        WebSearchService.ExtractResult extractResult = webSearchService.extractItemsFromWebPageAndSave(bread.getUserId(), "北京", webPage.getId());
        System.out.println(extractResult);
    }

    @Test
    void testNonPoiItem(){
        UUID userId = UUID.fromString("91b051a6-08ae-4125-a943-370e1deb5ce4");
        NonPoiItem nonPoiItem = new NonPoiItem();
        nonPoiItem.setPrivateUserId(userId);
        nonPoiItem.setType(NonPoiType.ACTIVITY);
        nonPoiItem.setTitle("北京 Visit");
        nonPoiItem.setDescription("北京 Visit");
        nonPoiItem.setSourceUrl("https://www.baidu.com");
        nonPoiItemService.save(nonPoiItem);
    }

    @Test
    void testMcpSyncClient(){
        McpSyncClient mcpClient = mcpSyncClients.getFirst();
        McpSchema.ListToolsResult listToolsResult = mcpClient.listTools();
        System.out.println(listToolsResult);
    }

}
