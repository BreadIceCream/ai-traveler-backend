package com.bread.traveler.controller;

import com.bread.traveler.dto.EntireTripDayItem;
import com.bread.traveler.dto.TripDayItemDto;
import com.bread.traveler.entity.TripDayItems;
import com.bread.traveler.service.TripDayItemsService;
import com.bread.traveler.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalTime;
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
    public Result getEntireItemsByTripDayId(@RequestParam UUID tripDayId) {
        List<EntireTripDayItem> items = tripDayItemsService.getEntireItemsByTripDayId(tripDayId);
        return items.isEmpty() ? Result.success("获取成功，日程为空", items) : Result.success("获取成功", items);
    }

    @PostMapping("/add")
    @Operation(summary = "添加日程item")
    public Result addItems(@RequestParam UUID tripDayId, @RequestParam UUID entityId,
                           @RequestParam boolean isPoi, @RequestBody TripDayItemDto dto) {
        TripDayItems tripDayItems = tripDayItemsService.addItems(tripDayId, entityId, isPoi, dto);
        return tripDayItems == null ? Result.serverError("添加失败") : Result.success("添加成功", tripDayItems);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除日程item")
    public Result deleteItems(@RequestParam List<UUID> itemIds){
        boolean result = tripDayItemsService.deleteItems(itemIds);
        return result ? Result.success("删除成功") : Result.serverError("删除失败");
    }

    @PutMapping("/update")
    @Operation(summary = "更新日程item信息")
    public Result updateItemInfo(@RequestBody TripDayItemDto dto) {
        TripDayItems updatedItem = tripDayItemsService.updateItemInfo(dto);
        return updatedItem == null ? Result.serverError("更新失败") : Result.success("更新成功", updatedItem);
    }

    @PutMapping("/transport")
    @Operation(summary = "AI更新日程item的交通建议")
    public Result updateTransportNote(@RequestParam UUID itemId, @RequestParam(required = false) String originAddress) {
        TripDayItems updatedItem = tripDayItemsService.updateTransportNote(itemId, originAddress);
        return updatedItem == null ? Result.serverError("更新失败") : Result.success("更新成功", updatedItem);
    }

    @PutMapping("/move")
    @Operation(summary = "移动日程item的位置，改变顺序")
    public Result moveItemOrder(@RequestParam UUID currentId,
                                @RequestParam(required = false) UUID prevId,
                                @RequestParam(required = false) UUID nextId,
                                @RequestParam UUID tripDayId) {
        boolean result = tripDayItemsService.moveItemOrder(currentId, prevId, nextId, tripDayId);
        return result ? Result.success("移动成功") : Result.serverError("移动失败");
    }



}
