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
    @Operation(summary = "获取会话下的所有网页", description = "获取会话下的所有网页")
    public Result list(
            @Schema(description = "会话ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID conversationId) {
        List<WebPage> webPages = webSearchService.listByConversationId(conversationId);
        return Result.success(webPages);
    }

}
