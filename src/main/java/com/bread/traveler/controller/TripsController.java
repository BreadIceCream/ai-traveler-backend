package com.bread.traveler.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bread.traveler.dto.EntireTrip;
import com.bread.traveler.dto.TripDto;
import com.bread.traveler.dto.TripWithMemberInfoDto;
import com.bread.traveler.entity.Trips;
import com.bread.traveler.enums.TripStatus;
import com.bread.traveler.service.TripsService;
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
@RequestMapping("/trips")
@Tag(name = "旅程")
public class TripsController {

    @Autowired
    private TripsService tripsService;

    @PostMapping("/create")
    @Operation(summary = "创建旅程", description = "创建旅程\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": {\n    \"tripId\": \"123e4567-e89b-12d3-a456-426614174000\",\n    \"userId\": \"123e4567-e89b-12d3-a456-426614174000\",\n    \"title\": \"北京三日游\",\n    \"destinationCity\": \"北京\",\n    \"startDate\": \"2024-12-01\",\n    \"endDate\": \"2024-12-03\",\n    \"totalBudget\": 3000.00,\n    \"status\": \"PLANNED\",\n    \"description\": \"北京三日游计划\",\n    \"createdAt\": \"2023-10-01T12:00:00+08:00\",\n    \"isPrivate\": false\n  }\n}\n```")
    public Result createTrip(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "旅程dto", example = "{\"title\": \"北京三日游\", \"destinationCity\": \"北京\", \"startDate\": \"2024-12-01\", \"endDate\": \"2024-12-03\", \"totalBudget\": 3000.00, \"description\": \"北京三日游计划\"}") @RequestBody TripDto dto) {
        Trips trip = tripsService.createTrip(userId, dto);
        return trip == null ? Result.serverError("创建失败") : Result.success("创建成功", trip);
    }

    @PutMapping("/update")
    @Operation(summary = "更新旅程信息", description = "更新旅程信息\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": {\n    \"tripId\": \"123e4567-e89b-12d3-a456-426614174000\",\n    \"userId\": \"123e4567-e89b-12d3-a456-426614174000\",\n    \"title\": \"北京三日游（更新）\",\n    \"destinationCity\": \"北京\",\n    \"startDate\": \"2024-12-01\",\n    \"endDate\": \"2024-12-03\",\n    \"totalBudget\": 3500.00,\n    \"status\": \"PLANNED\",\n    \"description\": \"更新后的北京三日游计划\",\n    \"createdAt\": \"2023-10-01T12:00:00+08:00\",\n    \"isPrivate\": false\n  }\n}\n```")
    public Result updateTripInfo(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId,
            @Schema(description = "旅程dto", example = "{\"title\": \"北京三日游（更新）\", \"destinationCity\": \"北京\", \"startDate\": \"2024-12-01\", \"endDate\": \"2024-12-03\", \"totalBudget\": 3500.00, \"description\": \"更新后的北京三日游计划\"}") @RequestBody TripDto dto) {
        Trips trip = tripsService.updateTripInfo(userId, tripId, dto);
        return trip == null ? Result.serverError("更新失败") : Result.success("更新成功", trip);
    }

    @PutMapping("/visibility")
    @Operation(summary = "更新旅程可见性", description = "更新旅程可见性\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": \"更新成功\"\n}\n```")
    public Result updateTripVisibility(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId,
            @Schema(description = "是否私有", example = "false") @RequestParam Boolean isPrivate) {
        boolean result = tripsService.changeVisibility(userId, tripId, isPrivate);
        return result ? Result.success("更新成功") : Result.serverError("更新失败");
    }

    @PutMapping("/status")
    @Operation(summary = "更新旅程状态", description = "更新旅程状态\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": \"更新成功\"\n}\n```")
    public Result updateTripStatus(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId,
            @Schema(description = "新状态", example = "IN_PROGRESS") @RequestParam TripStatus newStatus) {
        boolean result = tripsService.changeStatus(userId, tripId, newStatus);
        return result ? Result.success("更新成功") : Result.serverError("更新失败");
    }

    @GetMapping("/user")
    @Operation(summary = "获取用户所有旅程", description = "获取用户所有旅程\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": [\n    {\n      \"tripId\": \"123e4567-e89b-12d3-a456-426614174000\",\n      \"ownerId\": \"123e4567-e89b-12d3-a456-426614174000\",\n      \"title\": \"北京三日游\",\n      \"destinationCity\": \"北京\",\n      \"startDate\": \"2024-12-01\",\n      \"endDate\": \"2024-12-03\",\n      \"totalBudget\": 3000.00,\n      \"status\": \"IN_PROGRESS\",\n      \"description\": \"北京三日游计划\",\n      \"createdAt\": \"2023-10-01T12:00:00+08:00\",\n      \"isPrivate\": false,\n      \"memberRole\": \"OWNER\",\n      \"isPass\": true,\n      \"joinedAt\": \"2023-10-01T12:00:00+08:00\"\n    }\n  ]\n}\n```")
    public Result getAllTripsOfUser(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId) {
        List<TripWithMemberInfoDto> trips = tripsService.getAllTripsOfUser(userId);
        return trips.isEmpty() ? Result.success("获取成功，暂无旅程", trips) : Result.success("获取成功", trips);
    }

    @GetMapping("/public")
    @Operation(summary = "分页获取公开旅程", description = "分页获取公开旅程\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": {\n    \"records\": [\n      {\n        \"tripId\": \"123e4567-e89b-12d3-a456-426614174000\",\n        \"userId\": \"123e4567-e89b-12d3-a456-426614174000\",\n        \"title\": \"北京三日游\",\n        \"destinationCity\": \"北京\",\n        \"startDate\": \"2024-12-01\",\n        \"endDate\": \"2024-12-03\",\n        \"totalBudget\": 3000.00,\n        \"status\": \"PLANNED\",\n        \"description\": \"北京三日游计划\",\n        \"createdAt\": \"2023-10-01T12:00:00+08:00\",\n        \"isPrivate\": false\n      }\n    ],\n    \"total\": 100,\n    \"size\": 10,\n    \"current\": 1,\n    \"pages\": 10\n  }\n}\n```")
    public Result getPublicTrips(
            @Schema(description = "目的地城市", example = "北京") @RequestParam(required = false) String destinationCity,
            @Schema(description = "开始日期", example = "2024-12-01") @RequestParam(required = false) LocalDate startDate,
            @Schema(description = "结束日期", example = "2024-12-31") @RequestParam(required = false) LocalDate endDate,
            @Schema(description = "页码", example = "1") @RequestParam(defaultValue = "1") Integer pageNum,
            @Schema(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Trips> publicTrips = tripsService.getPublicTrips(destinationCity, startDate, endDate, pageNum, pageSize);
        return Result.success("获取成功", publicTrips);
    }

    @GetMapping("/detail")
    @Operation(summary = "获取完整旅程信息", description = "获取完整旅程信息\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": {\n    \"trip\": { ... },\n    \"tripDays\": [\n      {\n        \"tripDay\": { ... },\n        \"tripDayItems\": [ ... ]\n      }\n    ]\n  }\n}\n```")
    public Result getEntireTrip(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId) {
        EntireTrip entireTrip = tripsService.getEntireTrip(userId, tripId);
        return Result.success("获取成功", entireTrip);
    }

    @PostMapping("/ai-generate")
    @Operation(summary = "AI智能生成完整旅程", description = "AI智能生成完整旅程\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": {\n    \"trip\": { ... },\n    \"tripDays\": [\n      {\n        \"tripDay\": { ... },\n        \"tripDayItems\": [ ... ]\n      }\n    ]\n  }\n}\n```")
    public Result aiGenerateEntireTrip(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId) {
        EntireTrip result = tripsService.aiGenerateEntireTripPlan(userId, tripId);
        return result == null ? Result.serverError("AI规划失败") : Result.success("AI规划成功", result);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除旅程，级联删除所有日程和item", description = "删除旅程，级联删除所有日程和item\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": \"删除成功\"\n}\n```")
    public Result deleteTrip(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId) {
        boolean result = tripsService.deleteTrip(userId, tripId);
        return result ? Result.success("删除成功") : Result.serverError("删除失败");
    }
}