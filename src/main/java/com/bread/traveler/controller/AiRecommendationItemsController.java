package com.bread.traveler.controller;

import com.bread.traveler.entity.NonPoiItem;
import com.bread.traveler.entity.Pois;
import com.bread.traveler.service.AiRecommendationItemsService;
import com.bread.traveler.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recommendation/items")
@Tag(name = "AI推荐项管理")
public class AiRecommendationItemsController {

    @Autowired
    private AiRecommendationItemsService aiRecommendationItemsService;

    @GetMapping("/pois")
    @Operation(summary = "获取会话推荐的pois")
    public Result getPoisItems(UUID userId, UUID conversationId, @RequestParam(required = false) Boolean isManual) {
        List<Pois> poisItems = aiRecommendationItemsService.getPoisItems(userId, conversationId, isManual);
        return Result.success(poisItems);
    }

    @GetMapping("/nonPois")
    @Operation(summary = "获取会话推荐的nonPois")
    public Result getNonPoisItems(UUID userId, UUID conversationId, @RequestParam(required = false) Boolean isManual) {
        List<NonPoiItem> nonPoiItems = aiRecommendationItemsService.getNonPoiItems(userId, conversationId, isManual);
        return Result.success(nonPoiItems);
    }

    @PostMapping("/pois")
    @Operation(summary = "手动添加pois到会话推荐项中")
    public Result addPois(@RequestParam UUID userId, @RequestParam UUID conversationId, @RequestParam List<UUID> poiIds) {
        boolean success = aiRecommendationItemsService.addPois(userId, conversationId, poiIds, true);
        return success ? Result.success("添加成功") : Result.serverError("添加失败");
    }

    @PostMapping("/nonPois")
    @Operation(summary = "手动添加nonPois到会话推荐项中")
    public Result addNonPois(@RequestParam UUID userId, @RequestParam UUID conversationId, @RequestParam List<UUID> nonPoiItemIds) {
        boolean success = aiRecommendationItemsService.addNonPoiItems(userId, conversationId, nonPoiItemIds, true);
        return success ? Result.success("添加成功") : Result.serverError("添加失败");
    }

    @DeleteMapping("/pois")
    @Operation(summary = "删除会话推荐中的pois", description = "删除会话推荐中的pois，如果poiIds为空，则删除该会话推荐中的所有")
    public Result removePoisFromItems(UUID userId, UUID conversationId, @RequestParam(required = false) List<UUID> poiIds) {
        // 如果poiIds为空，则删除该会话推荐中的所有
        boolean success = aiRecommendationItemsService.removePoisFromItems(userId, conversationId, poiIds);
        return success ? Result.success("删除成功") : Result.serverError("删除失败");
    }

    @DeleteMapping("/nonPois")
    @Operation(summary = "删除会话推荐中的nonPois", description = "删除会话推荐中的nonPois，如果nonPoiItemIds为空，则删除该会话推荐中的所有")
    public Result removeNonPoisFromItems(UUID userId, UUID conversationId, @RequestParam(required = false) List<UUID> nonPoiItemIds) {
        // 如果nonPoiItemIds为空，则删除该会话推荐中的所有
        boolean success = aiRecommendationItemsService.removeNonPoiFromItems(userId, conversationId, nonPoiItemIds);
        return success ? Result.success("删除成功") : Result.serverError("删除失败");
    }

}
