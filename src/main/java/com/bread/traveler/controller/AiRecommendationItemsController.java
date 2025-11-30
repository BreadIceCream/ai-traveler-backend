package com.bread.traveler.controller;

import com.bread.traveler.entity.NonPoiItem;
import com.bread.traveler.entity.Pois;
import com.bread.traveler.service.AiRecommendationItemsService;
import com.bread.traveler.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Operation(summary = "获取会话推荐的pois", description = "获取会话推荐的pois\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": [\n    {\n      \"poiId\": \"123e4567-e89b-12d3-a456-426614174000\",\n      \"externalApiId\": \"B0FFH4XYZ\",\n      \"name\": \"故宫博物院\",\n      \"type\": \"museum\",\n      \"city\": \"北京\",\n      \"address\": \"北京市东城区景山前街4号\",\n      \"latitude\": 39.9163,\n      \"longitude\": 116.3972,\n      \"description\": \"明清两朝的皇宫...\",\n      \"openingHours\": \"08:30-17:00\",\n      \"avgVisitDuration\": 180,\n      \"avgCost\": \"60元\",\n      \"photos\": [\"url1\", \"url2\"],\n      \"phone\": \"010-85007421\",\n      \"rating\": \"4.8\",\n      \"createdAt\": \"2023-10-01T12:00:00+08:00\"\n    }\n  ]\n}\n```")
    public Result getPoisItems(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") UUID userId,
            @Schema(description = "会话ID", example = "123e4567-e89b-12d3-a456-426614174001") UUID conversationId,
            @Schema(description = "是否为手动添加", example = "false") @RequestParam(required = false) Boolean isManual) {
        List<Pois> poisItems = aiRecommendationItemsService.getPoisItems(userId, conversationId, isManual);
        return Result.success(poisItems);
    }

    @GetMapping("/nonPois")
    @Operation(summary = "获取会话推荐的nonPois", description = "获取会话推荐的nonPois\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": [\n    {\n      \"id\": \"123e4567-e89b-12d3-a456-426614174000\",\n      \"type\": \"ACTIVITY\",\n      \"title\": \"全聚德烤鸭店\",\n      \"description\": \"正宗北京烤鸭\",\n      \"city\": \"北京\",\n      \"activityTime\": \"10:00-22:00\",\n      \"estimatedAddress\": \"北京市东城区前门大街32号\",\n      \"extraInfo\": \"人均消费200-300元\",\n      \"sourceUrl\": \"https://example.com\",\n      \"createdAt\": \"2023-10-01T12:00:00+08:00\",\n      \"privateUserId\": \"123e4567-e89b-12d3-a456-426614174000\"\n    }\n  ]\n}\n```")
    public Result getNonPoisItems(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") UUID userId,
            @Schema(description = "会话ID", example = "123e4567-e89b-12d3-a456-426614174001") UUID conversationId,
            @Schema(description = "是否为手动添加", example = "false") @RequestParam(required = false) Boolean isManual) {
        List<NonPoiItem> nonPoiItems = aiRecommendationItemsService.getNonPoiItems(userId, conversationId, isManual);
        return Result.success(nonPoiItems);
    }

    @PostMapping("/pois")
    @Operation(summary = "手动添加pois到会话推荐项中", description = "手动添加pois到会话推荐项中\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": \"添加成功\"\n}\n```")
    public Result addPois(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "会话ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID conversationId,
            @Schema(description = "POI ID列表", example = "[\"123e4567-e89b-12d3-a456-426614174002\", \"123e4567-e89b-12d3-a456-426614174003\"]") @RequestParam List<UUID> poiIds) {
        boolean success = aiRecommendationItemsService.addPois(userId, conversationId, poiIds, true);
        return success ? Result.success("添加成功") : Result.serverError("添加失败");
    }

    @PostMapping("/nonPois")
    @Operation(summary = "手动添加nonPois到会话推荐项中", description = "手动添加nonPois到会话推荐项中\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": \"添加成功\"\n}\n```")
    public Result addNonPois(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "会话ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID conversationId,
            @Schema(description = "非POI项ID列表", example = "[\"123e4567-e89b-12d3-a456-426614174004\", \"123e4567-e89b-12d3-a456-426614174005\"]") @RequestParam List<UUID> nonPoiItemIds) {
        boolean success = aiRecommendationItemsService.addNonPoiItems(userId, conversationId, nonPoiItemIds, true);
        return success ? Result.success("添加成功") : Result.serverError("添加失败");
    }

    @DeleteMapping("/pois")
    @Operation(summary = "删除会话推荐中的pois", description = "删除会话推荐中的pois，如果poiIds为空，则删除该会话推荐中的所有\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": \"删除成功\"\n}\n```")
    public Result removePoisFromItems(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") UUID userId,
            @Schema(description = "会话ID", example = "123e4567-e89b-12d3-a456-426614174001") UUID conversationId,
            @Schema(description = "POI ID列表", example = "[\"123e4567-e89b-12d3-a456-426614174002\"]") @RequestParam(required = false) List<UUID> poiIds) {
        // 如果poiIds为空，则删除该会话推荐中的所有
        boolean success = aiRecommendationItemsService.removePoisFromItems(userId, conversationId, poiIds);
        return success ? Result.success("删除成功") : Result.serverError("删除失败");
    }

    @DeleteMapping("/nonPois")
    @Operation(summary = "删除会话推荐中的nonPois", description = "删除会话推荐中的nonPois，如果nonPoiItemIds为空，则删除该会话推荐中的所有\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": \"删除成功\"\n}\n```")
    public Result removeNonPoisFromItems(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") UUID userId,
            @Schema(description = "会话ID", example = "123e4567-e89b-12d3-a456-426614174001") UUID conversationId,
            @Schema(description = "非POI项ID列表", example = "[\"123e4567-e89b-12d3-a456-426614174004\"]") @RequestParam(required = false) List<UUID> nonPoiItemIds) {
        // 如果nonPoiItemIds为空，则删除该会话推荐中的所有
        boolean success = aiRecommendationItemsService.removeNonPoiFromItems(userId, conversationId, nonPoiItemIds);
        return success ? Result.success("删除成功") : Result.serverError("删除失败");
    }

}
