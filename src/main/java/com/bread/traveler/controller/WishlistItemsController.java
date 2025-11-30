package com.bread.traveler.controller;

import com.bread.traveler.dto.EntireWishlistItem;
import com.bread.traveler.entity.WishlistItems;
import com.bread.traveler.service.WishlistItemsService;
import com.bread.traveler.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/wishlistItems")
@Tag(name = "心愿单服务")
public class WishlistItemsController {

    @Autowired
    private WishlistItemsService wishlistItemsService;

    @PostMapping("/add")
    @Operation(summary = "添加心愿单item", description = "添加心愿单item\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": \"添加成功\"\n}\n```")
    public Result addToWishList(
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId,
            @Schema(description = "实体ID", example = "123e4567-e89b-12d3-a456-426614174002") @RequestParam UUID entityId,
            @Schema(description = "是否为POI", example = "true") @RequestParam boolean isPoi) {
        boolean result = wishlistItemsService.addToWishList(tripId, entityId, isPoi);
        return result ? Result.success("添加成功") : Result.serverError("添加失败");
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除心愿单item", description = "删除心愿单item\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": \"删除成功\"\n}\n```")
    public Result removeFromWishList(
            @Schema(description = "心愿单item ID列表", example = "[\"123e4567-e89b-12d3-a456-426614174003\"]") @RequestParam List<UUID> itemIds) {
        boolean result = wishlistItemsService.removeFromWishList(itemIds);
        return result ? Result.success("删除成功") : Result.serverError("删除失败");
    }

    @GetMapping("/list")
    @Operation(summary = "获取某个行程下的心愿单item列表", description = "心愿单列表，包括每个item对应的entity信息。按照添加的顺序倒序排序\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": [\n    {\n      \"item\": {\n        \"itemId\": \"123e4567-e89b-12d3-a456-426614174000\",\n        \"tripId\": \"123e4567-e89b-12d3-a456-426614174001\",\n        \"entityId\": \"123e4567-e89b-12d3-a456-426614174002\",\n        \"isPoi\": true,\n        \"createdAt\": \"2023-10-01T12:00:00+08:00\"\n      },\n      \"entity\": {\n        \"poiId\": \"123e4567-e89b-12d3-a456-426614174002\",\n        \"name\": \"故宫博物院\",\n        \"type\": \"museum\",\n        \"city\": \"北京\",\n        \"address\": \"北京市东城区景山前街4号\",\n        \"latitude\": 39.9163,\n        \"longitude\": 116.3972,\n        \"description\": \"明清两朝的皇宫...\",\n        \"openingHours\": \"08:30-17:00\",\n        \"avgVisitDuration\": 180,\n        \"avgCost\": \"60元\",\n        \"photos\": [\"url1\"],\n        \"phone\": \"010-85007421\",\n        \"rating\": \"4.8\",\n        \"createdAt\": \"2023-10-01T12:00:00+08:00\"\n      }\n    }\n  ]\n}\n```")
    public Result listByTripId(
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId) {
        List<EntireWishlistItem> items = wishlistItemsService.listEntireByTripId(tripId);
        return items.isEmpty() ? Result.success("获取成功，心愿单为空", items) : Result.success("获取成功", items);
    }

}
