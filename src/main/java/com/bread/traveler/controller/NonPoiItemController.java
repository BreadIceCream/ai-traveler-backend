package com.bread.traveler.controller;

import com.bread.traveler.dto.NonPoiItemDto;
import com.bread.traveler.entity.NonPoiItem;
import com.bread.traveler.service.NonPoiItemService;
import com.bread.traveler.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/nonPoiItem")
@Tag(name = "非POI项管理")
public class NonPoiItemController {

    @Autowired
    private NonPoiItemService nonPoiItemService;

    @PostMapping("/create")
    @Operation(summary = "创建非POI项", description = "创建非POI项\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": {\n    \"id\": \"123e4567-e89b-12d3-a456-426614174000\",\n    \"type\": \"ACTIVITY\",\n    \"title\": \"全聚德烤鸭店\",\n    \"description\": \"正宗北京烤鸭\",\n    \"city\": \"北京\",\n    \"activityTime\": \"10:00-22:00\",\n    \"estimatedAddress\": \"北京市东城区前门大街32号\",\n    \"extraInfo\": \"人均消费200-300元\",\n    \"sourceUrl\": \"https://example.com\",\n    \"createdAt\": \"2023-10-01T12:00:00+08:00\",\n    \"privateUserId\": \"123e4567-e89b-12d3-a456-426614174000\"\n  }\n}\n```")
    public Result createNonPoiItem(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "非POI项dto", example = "{\"name\": \"测试非POI项\", \"type\": \"RESTAURANT\"}") @RequestBody NonPoiItemDto dto) {
        NonPoiItem nonPoiItem = nonPoiItemService.createNonPoiItem(userId, dto);
        return nonPoiItem != null ? Result.success("创建成功", nonPoiItem) : Result.serverError("创建失败");
    }

    @GetMapping("/list")
    @Operation(summary = "获取用户的非POI项列表", description = "获取用户的非POI项列表\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": [\n    {\n      \"id\": \"123e4567-e89b-12d3-a456-426614174000\",\n      \"type\": \"ACTIVITY\",\n      \"title\": \"全聚德烤鸭店\",\n      \"description\": \"正宗北京烤鸭\",\n      \"city\": \"北京\",\n      \"activityTime\": \"10:00-22:00\",\n      \"estimatedAddress\": \"北京市东城区前门大街32号\",\n      \"extraInfo\": \"人均消费200-300元\",\n      \"sourceUrl\": \"https://example.com\",\n      \"createdAt\": \"2023-10-01T12:00:00+08:00\",\n      \"privateUserId\": \"123e4567-e89b-12d3-a456-426614174000\"\n    }\n  ]\n}\n```")
    public Result getNonPoiItemList(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId) {
        List<NonPoiItem> nonPoiItemList = nonPoiItemService.getByUserId(userId);
        return Result.success(nonPoiItemList);
    }

    @PutMapping("/update")
    @Operation(summary = "更新非POI项", description = "更新非POI项\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": {\n    \"id\": \"123e4567-e89b-12d3-a456-426614174000\",\n    \"type\": \"ACTIVITY\",\n    \"title\": \"全聚德烤鸭店\",\n    \"description\": \"正宗北京烤鸭\",\n    \"city\": \"北京\",\n    \"activityTime\": \"10:00-22:00\",\n    \"estimatedAddress\": \"北京市东城区前门大街32号\",\n    \"extraInfo\": \"人均消费200-300元\",\n    \"sourceUrl\": \"https://example.com\",\n    \"createdAt\": \"2023-10-01T12:00:00+08:00\",\n    \"privateUserId\": \"123e4567-e89b-12d3-a456-426614174000\"\n  }\n}\n```")
    public Result updateNonPoiItem(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "非POI项dto", example = "{\"id\": \"123e4567-e89b-12d3-a456-426614174002\", \"name\": \"更新后的名称\", \"type\": \"RESTAURANT\"}") @RequestBody NonPoiItemDto dto) {
        NonPoiItem nonPoiItem = nonPoiItemService.updateNonPoiItem(userId, dto);
        return nonPoiItem != null ? Result.success("更新成功", nonPoiItem) : Result.serverError("更新失败");
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除非POI项", description = "删除非POI项\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": \"删除成功\"\n}\n```")
    public Result deleteNonPoiItem(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "非POI项ID", example = "123e4567-e89b-12d3-a456-426614174002") @RequestParam UUID nonPoiItemId) {
        boolean success = nonPoiItemService.deleteByIds(userId, List.of(nonPoiItemId));
        return success ? Result.success("删除成功") : Result.serverError("删除失败");
    }

}
