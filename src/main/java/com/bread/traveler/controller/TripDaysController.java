package com.bread.traveler.controller;

import com.bread.traveler.dto.EntireTripDay;
import com.bread.traveler.entity.TripDays;
import com.bread.traveler.service.TripDaysService;
import com.bread.traveler.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tripDays")
@Tag(name = "旅程日程")
public class TripDaysController {

    @Autowired
    private TripDaysService tripDaysService;

    @PostMapping("/create")
    @Operation(summary = "创建旅程日程")
    public Result createTripDay(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") 
            @RequestParam UUID userId,
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") 
            @RequestParam UUID tripId,
            @Schema(description = "日期", example = "2024-12-01") 
            @RequestParam LocalDate date,
            @Schema(description = "备注", example = "第一天的行程安排") 
            @RequestParam(required = false) String note) {
        TripDays tripDay = tripDaysService.createTripDay(userId, tripId, date, note);
        return tripDay == null ? Result.serverError("创建失败") : Result.success("创建成功", tripDay);
    }

    @PutMapping("/note")
    @Operation(summary = "更新日程备注")
    public Result updateTripDayNote(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") 
            @RequestParam UUID userId, 
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") 
            @RequestParam UUID tripId, 
            @Schema(description = "日程ID", example = "123e4567-e89b-12d3-a456-426614174002") 
            @RequestParam UUID tripDayId, 
            @Schema(description = "备注", example = "更新后的备注信息") 
            @RequestParam String note) {
        boolean result = tripDaysService.updateTripDayNote(userId, tripId, tripDayId, note);
        return result ? Result.success("更新成功") : Result.serverError("更新失败");
    }

    @GetMapping("/detail")
    @Operation(summary = "获取某天日程详情")
    public Result getEntireTripDay(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") 
            @RequestParam UUID userId, 
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") 
            @RequestParam UUID tripId, 
            @Schema(description = "日程ID", example = "123e4567-e89b-12d3-a456-426614174002") 
            @RequestParam UUID tripDayId) {
        EntireTripDay entireTripDay = tripDaysService.getEntireTripDay(userId, tripId, tripDayId);
        return Result.success("获取成功", entireTripDay);
    }

    @GetMapping("/list")
    @Operation(summary = "获取旅程所有日程")
    public Result getEntireTripDaysByTripId(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") 
            @RequestParam UUID userId, 
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") 
            @RequestParam UUID tripId) {
        List<EntireTripDay> tripDays = tripDaysService.getEntireTripDaysByTripId(userId, tripId);
        return tripDays.isEmpty() ? Result.success("获取成功，暂无日程", tripDays) : Result.success("获取成功", tripDays);
    }

    @PostMapping("/aiReplan")
    @Operation(summary = "AI智能重新规划某天旅程")
    public Result aiRePlanTripDay(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") 
            @RequestParam UUID userId, 
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") 
            @RequestParam UUID tripId,
            @Schema(description = "日程ID", example = "123e4567-e89b-12d3-a456-426614174002") 
            @RequestParam UUID tripDayId) {
        EntireTripDay result = tripDaysService.aiRePlanTripDay(userId, tripId, tripDayId);
        return result == null ? Result.serverError("重新规划失败") : Result.success("重新规划成功", result);
    }

    @PutMapping("/exchange")
    @Operation(summary = "交换两个日程的顺序")
    public Result exchangeDayOrder(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") 
            @RequestParam UUID userId, 
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") 
            @RequestParam UUID tripId, 
            @Schema(description = "第一个日程ID", example = "123e4567-e89b-12d3-a456-426614174002") 
            @RequestParam UUID aTripDayId, 
            @Schema(description = "第二个日程ID", example = "123e4567-e89b-12d3-a456-426614174003") 
            @RequestParam UUID bTripDayId) {
        boolean result = tripDaysService.exchangeDayOrder(userId, tripId, aTripDayId, bTripDayId);
        return result ? Result.success("交换成功") : Result.serverError("交换失败");
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除日程，级联删除所有item")
    public Result deleteTripDays(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") 
            @RequestParam UUID userId, 
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") 
            @RequestParam UUID tripId, 
            @Schema(description = "日程ID列表", example = "[\"123e4567-e89b-12d3-a456-426614174002\"]") 
            @RequestParam List<UUID> tripDayIds) {
        boolean result = tripDaysService.deleteTripDays(userId, tripId, tripDayIds);
        return result ? Result.success("删除成功") : Result.serverError("删除失败");
    }
}
