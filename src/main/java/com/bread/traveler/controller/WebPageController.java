package com.bread.traveler.controller;

import com.bread.traveler.entity.WebPage;
import com.bread.traveler.service.WebSearchService;
import com.bread.traveler.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/webPage")
@Tag(name = "网页管理")
public class WebPageController {

    @Autowired
    private WebSearchService webSearchService;

    @GetMapping("/list")
    @Operation(summary = "获取会话下的所有网页", description = "获取会话下的所有网页\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": [\n    {\n      \"id\": \"123e4567-e89b-12d3-a456-426614174000\",\n      \"conversationId\": \"123e4567-e89b-12d3-a456-426614174001\",\n      \"name\": \"故宫博物院官网\",\n      \"url\": \"https://www.dpm.org.cn\",\n      \"displayUrl\": \"www.dpm.org.cn\",\n      \"snippet\": \"故宫博物院官方网站...\",\n      \"summary\": \"详细介绍故宫的历史和文化...\",\n      \"siteName\": \"故宫博物院\",\n      \"datePublished\": \"2023-10-01T12:00:00+08:00\",\n      \"createdAt\": \"2023-10-01T12:00:00+08:00\"\n    }\n  ]\n}\n```")
    public Result list(
            @Schema(description = "会话ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID conversationId) {
        List<WebPage> webPages = webSearchService.listByConversationId(conversationId);
        return Result.success(webPages);
    }

}
