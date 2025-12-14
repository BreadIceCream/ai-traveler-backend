package com.bread.traveler.controller;

import com.bread.traveler.dto.ExtractWebPageDto;
import com.bread.traveler.entity.WebPage;
import com.bread.traveler.service.WebSearchService;
import com.bread.traveler.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
            @Schema(description = "会话ID", example = "123e4567-e89b-12d3-a456-426614174001")
            @RequestParam UUID conversationId) {
        List<WebPage> webPages = webSearchService.listByConversationId(conversationId);
        return Result.success(webPages);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除网页", description = "删除网页")
    public Result delete(
            @Schema(description = "网页ID", example = "123e4567-e89b-12d3-a456-426614174001")
            @RequestParam UUID webPageId) {
        boolean result = webSearchService.deleteById(webPageId);
        return result ? Result.success("删除成功", null) : Result.businessError("删除失败");
    }

    @PostMapping("/extract")
    @Operation(summary = "从网页中提取信息", description = "从网页中提取信息，转为POI和非POI项并保存到AI推荐项当中")
    public Result extract(@RequestAttribute("userId") UUID userId, @RequestBody ExtractWebPageDto dto){
        List<WebSearchService.ExtractResult> extractResults = webSearchService.extractItemsFromWebPageAndSave(userId, dto.getCity(), dto.getWebPageIds());
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        List<WebSearchService.ExtractResult> extractResults = List.of(new WebSearchService.ExtractResult(
//                "提取成功，已保存至AI推荐项.",
//                dto.getWebPageIds().getFirst(),
//                "MOCK" + Math.random(),
//                new ArrayList<>(),
//                new ArrayList<>()
//        ));
        return Result.success(extractResults);
    }

}
