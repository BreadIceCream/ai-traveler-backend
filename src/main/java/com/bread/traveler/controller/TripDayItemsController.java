package com.bread.traveler.controller;

import com.bread.traveler.dto.EntireTripDayItem;
import com.bread.traveler.dto.TripDayItemDto;
import com.bread.traveler.entity.TripDayItems;
import com.bread.traveler.service.TripDayItemsService;
import com.bread.traveler.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tripDayItems")
@Tag(name = "日程item")
public class TripDayItemsController {

    @Autowired
    private TripDayItemsService tripDayItemsService;

    @GetMapping("/list")
    @Operation(summary = "获取某个日程的详细item列表", description = "获取某个日程的详细item列表，包括entity信息")
    public Result getEntireItemsByTripDayId(
            @Schema(description = "用户ID") @RequestAttribute("userId") UUID userId,
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId,
            @Schema(description = "日程ID", example = "123e4567-e89b-12d3-a456-426614174002") @RequestParam UUID tripDayId) {
        List<EntireTripDayItem> items = tripDayItemsService.getEntireItemsByTripDayId(userId, tripId, tripDayId);
        return items.isEmpty() ? Result.success("获取成功，日程为空", items) : Result.success("获取成功", items);
    }

    @PostMapping("/add")
    @Operation(summary = "添加日程item", description = "添加日程item")
    public Result addItems(
            @Schema(description = "用户ID") @RequestAttribute("userId") UUID userId,
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId,
            @Schema(description = "日程ID", example = "123e4567-e89b-12d3-a456-426614174002") @RequestParam UUID tripDayId,
            @Schema(description = "实体ID", example = "123e4567-e89b-12d3-a456-426614174003") @RequestParam UUID entityId,
            @Schema(description = "是否为POI", example = "true") @RequestParam boolean isPoi,
            @RequestBody TripDayItemDto dto) {
        TripDayItems tripDayItems = tripDayItemsService.addItems(userId, tripId, tripDayId, entityId, isPoi, dto);
        return tripDayItems == null ? Result.serverError("添加失败") : Result.success("添加成功", tripDayItems);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除日程item", description = "删除日程item")
    public Result deleteItems(
            @Schema(description = "用户ID") @RequestAttribute("userId") UUID userId,
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId,
            @Schema(description = "日程item ID列表") @RequestParam List<UUID> itemIds) {
        boolean result = tripDayItemsService.deleteItems(userId, tripId, itemIds);
        return result ? Result.success("删除成功", null) : Result.serverError("删除失败");
    }

    @PutMapping("/update")
    @Operation(summary = "更新日程item信息", description = "更新日程item信息")
    public Result updateItemInfo(
            @Schema(description = "用户ID") @RequestAttribute("userId") UUID userId,
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId,
            @RequestBody TripDayItemDto dto) {
        TripDayItems updatedItem = tripDayItemsService.updateItemInfo(userId, tripId, dto);
        return updatedItem == null ? Result.serverError("更新失败") : Result.success("更新成功", updatedItem);
    }

    @PutMapping("/transport")
    @Operation(summary = "AI更新日程item的交通建议", description = "AI更新日程item的交通建议")
    public Result updateTransportNote(
            @Schema(description = "用户ID") @RequestAttribute("userId") UUID userId,
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId,
            @Schema(description = "日程item ID", example = "123e4567-e89b-12d3-a456-426614174004") @RequestParam UUID itemId,
            @Schema(description = "起始地址", example = "北京市朝阳区") @RequestParam(required = false) String originAddress) {
        TripDayItems updatedItem = tripDayItemsService.updateTransportNote(userId, tripId, itemId, originAddress);
        return updatedItem == null ? Result.serverError("更新失败") : Result.success("更新成功", updatedItem);
    }

    @PutMapping("/move")
    @Operation(summary = "移动日程item的位置，改变顺序", description = "移动日程item的位置，改变顺序")
    public Result moveItemOrder(
            @Schema(description = "用户ID") @RequestAttribute("userId") UUID userId,
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId,
            @Schema(description = "当前item ID", example = "123e4567-e89b-12d3-a456-426614174004") @RequestParam UUID currentId,
            @Schema(description = "前一个item ID", example = "123e4567-e89b-12d3-a456-426614174003") @RequestParam(required = false) UUID prevId,
            @Schema(description = "后一个item ID", example = "123e4567-e89b-12d3-a456-426614174005") @RequestParam(required = false) UUID nextId,
            @Schema(description = "日程ID", example = "123e4567-e89b-12d3-a456-426614174002") @RequestParam UUID tripDayId) {
        boolean result = tripDayItemsService.moveItemOrder(userId, tripId, currentId, prevId, nextId, tripDayId);
        return result ? Result.success("移动成功", null) : Result.serverError("移动失败");
    }

}
