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
    @Operation(summary = "创建旅程日程", description = "创建旅程日程\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": {\n    \"tripDayId\": \"123e4567-e89b-12d3-a456-426614174000\",\n    \"tripId\": \"123e4567-e89b-12d3-a456-426614174001\",\n    \"dayDate\": \"2024-12-01\",\n    \"notes\": \"第一天的行程安排\"\n  }\n}\n```")
    public Result createTripDay(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId,
            @Schema(description = "日期", example = "2024-12-01") @RequestParam LocalDate date,
            @Schema(description = "备注", example = "第一天的行程安排") @RequestParam(required = false) String note) {
        TripDays tripDay = tripDaysService.createTripDay(userId, tripId, date, note);
        return tripDay == null ? Result.serverError("创建失败") : Result.success("创建成功", tripDay);
    }

    @PutMapping("/note")
    @Operation(summary = "更新日程备注", description = "更新日程备注\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": \"更新成功\"\n}\n```")
    public Result updateTripDayNote(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId,
            @Schema(description = "日程ID", example = "123e4567-e89b-12d3-a456-426614174002") @RequestParam UUID tripDayId,
            @Schema(description = "备注", example = "更新后的备注信息") @RequestParam String note) {
        boolean result = tripDaysService.updateTripDayNote(userId, tripId, tripDayId, note);
        return result ? Result.success("更新成功") : Result.serverError("更新失败");
    }

    @GetMapping("/detail")
    @Operation(summary = "获取某天日程详情", description = "获取某天日程详情\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": {\n    \"tripDay\": {\n      \"tripDayId\": \"123e4567-e89b-12d3-a456-426614174000\",\n      \"tripId\": \"123e4567-e89b-12d3-a456-426614174001\",\n      \"dayDate\": \"2024-12-01\",\n      \"notes\": \"第一天的行程安排\"\n    },\n    \"tripDayItems\": [\n      {\n        \"item\": {\n          \"itemId\": \"123e4567-e89b-12d3-a456-426614174000\",\n          \"tripDayId\": \"123e4567-e89b-12d3-a456-426614174001\",\n          \"entityId\": \"123e4567-e89b-12d3-a456-426614174002\",\n          \"startTime\": \"09:00:00\",\n          \"endTime\": \"10:30:00\",\n          \"itemOrder\": 1.0,\n          \"transportNotes\": \"步行10分钟\",\n          \"estimatedCost\": 50.00,\n          \"isPoi\": true,\n          \"notes\": \"需要提前预约\"\n        },\n        \"entity\": {\n          \"poiId\": \"123e4567-e89b-12d3-a456-426614174002\",\n          \"name\": \"故宫博物院\",\n          \"type\": \"museum\",\n          \"city\": \"北京\",\n          \"address\": \"北京市东城区景山前街4号\",\n          \"latitude\": 39.9163,\n          \"longitude\": 116.3972,\n          \"description\": \"明清两朝的皇宫...\",\n          \"openingHours\": \"08:30-17:00\",\n          \"avgVisitDuration\": 180,\n          \"avgCost\": \"60元\",\n          \"photos\": [\"url1\"],\n          \"phone\": \"010-85007421\",\n          \"rating\": \"4.8\",\n          \"createdAt\": \"2023-10-01T12:00:00+08:00\"\n        }\n      }\n    ]\n  }\n}\n```")
    public Result getEntireTripDay(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId,
            @Schema(description = "日程ID", example = "123e4567-e89b-12d3-a456-426614174002") @RequestParam UUID tripDayId) {
        EntireTripDay entireTripDay = tripDaysService.getEntireTripDay(userId, tripId, tripDayId);
        return Result.success("获取成功", entireTripDay);
    }

    @GetMapping("/list")
    @Operation(summary = "获取旅程所有日程", description = "获取旅程所有日程\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": [\n    {\n      \"tripDay\": {\n        \"tripDayId\": \"123e4567-e89b-12d3-a456-426614174000\",\n        \"tripId\": \"123e4567-e89b-12d3-a456-426614174001\",\n        \"dayDate\": \"2024-12-01\",\n        \"notes\": \"第一天的行程安排\"\n      },\n      \"tripDayItems\": [\n        {\n          \"item\": { ... },\n          \"entity\": { ... }\n        }\n      ]\n    }\n  ]\n}\n```")
    public Result getEntireTripDaysByTripId(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId) {
        List<EntireTripDay> tripDays = tripDaysService.getEntireTripDaysByTripId(userId, tripId);
        return tripDays.isEmpty() ? Result.success("获取成功，暂无日程", tripDays) : Result.success("获取成功", tripDays);
    }

    @PostMapping("/aiReplan")
    @Operation(summary = "AI智能重新规划某天旅程", description = "AI智能重新规划某天旅程\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": {\n    \"tripDay\": {\n      \"tripDayId\": \"123e4567-e89b-12d3-a456-426614174000\",\n      \"tripId\": \"123e4567-e89b-12d3-a456-426614174001\",\n      \"dayDate\": \"2024-12-01\",\n      \"notes\": \"第一天的行程安排\"\n    },\n    \"tripDayItems\": [\n      {\n        \"item\": { ... },\n        \"entity\": { ... }\n      }\n    ]\n  }\n}\n```")
    public Result aiRePlanTripDay(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId,
            @Schema(description = "日程ID", example = "123e4567-e89b-12d3-a456-426614174002") @RequestParam UUID tripDayId) {
        EntireTripDay result = tripDaysService.aiRePlanTripDay(userId, tripId, tripDayId);
        return result == null ? Result.serverError("重新规划失败") : Result.success("重新规划成功", result);
    }

    @PutMapping("/exchange")
    @Operation(summary = "交换两个日程的顺序", description = "交换两个日程的顺序\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": \"交换成功\"\n}\n```")
    public Result exchangeDayOrder(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId,
            @Schema(description = "第一个日程ID", example = "123e4567-e89b-12d3-a456-426614174002") @RequestParam UUID aTripDayId,
            @Schema(description = "第二个日程ID", example = "123e4567-e89b-12d3-a456-426614174003") @RequestParam UUID bTripDayId) {
        boolean result = tripDaysService.exchangeDayOrder(userId, tripId, aTripDayId, bTripDayId);
        return result ? Result.success("交换成功") : Result.serverError("交换失败");
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除日程，级联删除所有item", description = "删除日程，级联删除所有item\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": \"删除成功\"\n}\n```")
    public Result deleteTripDays(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId,
            @Schema(description = "日程ID列表", example = "[\"123e4567-e89b-12d3-a456-426614174002\"]") @RequestParam List<UUID> tripDayIds) {
        boolean result = tripDaysService.deleteTripDays(userId, tripId, tripDayIds);
        return result ? Result.success("删除成功") : Result.serverError("删除失败");
    }
}
