package com.bread.traveler.controller;

import com.bread.traveler.dto.TripNoteLogCreateDto;
import com.bread.traveler.entity.TripLogs;
import com.bread.traveler.enums.LogType;
import com.bread.traveler.service.TripLogsService;
import com.bread.traveler.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/trip-logs")
@Tag(name = "旅程日志")
public class TripLogsController {

    @Autowired
    private TripLogsService tripLogsService;

    @PostMapping("/note")
    @Operation(summary = "创建文本日志", description = "创建文本日志\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": {\n    \"logId\": \"123e4567-e89b-12d3-a456-426614174000\",\n    \"tripId\": \"123e4567-e89b-12d3-a456-426614174001\",\n    \"logType\": \"NOTE\",\n    \"content\": \"今天游览了故宫博物院\",\n    \"createdAt\": \"2023-10-01T12:00:00+08:00\",\n    \"userId\": \"123e4567-e89b-12d3-a456-426614174000\",\n    \"isPublic\": true\n  }\n}\n```")
    public Result createNoteLog(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId,
            @RequestBody TripNoteLogCreateDto dto) {
        TripLogs log = tripLogsService.createNoteLog(userId, tripId, dto.getContent(), dto.getIsPublic());
        return log == null ? Result.serverError("创建失败") : Result.success("创建成功", log);
    }

    @PostMapping("/image")
    @Operation(summary = "创建图片日志", description = "上传图片文件，最大为3MB\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": {\n    \"logId\": \"123e4567-e89b-12d3-a456-426614174000\",\n    \"tripId\": \"123e4567-e89b-12d3-a456-426614174001\",\n    \"logType\": \"IMAGE\",\n    \"content\": \"http://example.com/image.jpg\",\n    \"createdAt\": \"2023-10-01T12:00:00+08:00\",\n    \"userId\": \"123e4567-e89b-12d3-a456-426614174000\",\n    \"isPublic\": true\n  }\n}\n```")
    public Result createImgLog(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId,
            @Schema(description = "图片文件", example = "图片文件") @RequestParam("imgFile") MultipartFile imgFile,
            @Schema(description = "是否公开", example = "true") @RequestParam(required = false) Boolean isPublic) {
        TripLogs log = tripLogsService.createImgLog(userId, tripId, imgFile, isPublic);
        return log == null ? Result.serverError("创建失败") : Result.success("创建成功", log);
    }

    @PutMapping("/visibility")
    @Operation(summary = "修改日志可见性", description = "修改日志可见性\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": \"修改成功\"\n}\n```")
    public Result updateLogVisibility(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "日志ID", example = "123e4567-e89b-12d3-a456-426614174002") @RequestParam UUID logId,
            @Schema(description = "是否公开", example = "false") @RequestParam Boolean isPublic) {
        boolean result = tripLogsService.changeLogVisibility(userId, logId, isPublic);
        return result ? Result.success("修改成功") : Result.serverError("修改失败");
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除日志", description = "删除日志\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": \"删除成功\"\n}\n```")
    public Result deleteLog(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "日志ID", example = "123e4567-e89b-12d3-a456-426614174002") @RequestParam UUID logId) {
        boolean result = tripLogsService.deleteLog(userId, logId);
        return result ? Result.success("删除成功") : Result.serverError("删除失败");
    }

    @GetMapping("/trip")
    @Operation(summary = "获取当前用户某个旅程所有日志", description = "获取当前用户某个旅程所有日志\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": [\n    {\n      \"logId\": \"123e4567-e89b-12d3-a456-426614174000\",\n      \"tripId\": \"123e4567-e89b-12d3-a456-426614174001\",\n      \"logType\": \"NOTE\",\n      \"content\": \"今天游览了故宫博物院\",\n      \"createdAt\": \"2023-10-01T12:00:00+08:00\",\n      \"userId\": \"123e4567-e89b-12d3-a456-426614174000\",\n      \"isPublic\": true\n    }\n  ]\n}\n```")
    public Result getLogsByTripId(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId) {
        List<TripLogs> logs = tripLogsService.getLogsOfUserByTripId(userId, tripId);
        return logs.isEmpty() ? Result.success("获取成功，暂无日志", logs) : Result.success("获取成功", logs);
    }

    @GetMapping("/trip/type")
    @Operation(summary = "获取当前用户某个旅程指定类型日志", description = "获取当前用户某个旅程指定类型日志\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": [\n    {\n      \"logId\": \"123e4567-e89b-12d3-a456-426614174000\",\n      \"tripId\": \"123e4567-e89b-12d3-a456-426614174001\",\n      \"logType\": \"NOTE\",\n      \"content\": \"今天游览了故宫博物院\",\n      \"createdAt\": \"2023-10-01T12:00:00+08:00\",\n      \"userId\": \"123e4567-e89b-12d3-a456-426614174000\",\n      \"isPublic\": true\n    }\n  ]\n}\n```")
    public Result getLogsByTripIdAndType(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId,
            @Schema(description = "日志类型", example = "NOTE") @RequestParam LogType type) {
        List<TripLogs> logs = tripLogsService.getLogsOfUserByTripIdAndType(userId, tripId, type);
        return logs.isEmpty() ? Result.success("获取成功，暂无日志", logs) : Result.success("获取成功", logs);
    }

    @GetMapping("/trip/public")
    @Operation(summary = "获取旅程公开日志", description = "获取旅程公开日志\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": [\n    {\n      \"logId\": \"123e4567-e89b-12d3-a456-426614174000\",\n      \"tripId\": \"123e4567-e89b-12d3-a456-426614174001\",\n      \"logType\": \"NOTE\",\n      \"content\": \"今天游览了故宫博物院\",\n      \"createdAt\": \"2023-10-01T12:00:00+08:00\",\n      \"userId\": \"123e4567-e89b-12d3-a456-426614174000\",\n      \"isPublic\": true\n    }\n  ]\n}\n```")
    public Result getPublicLogsByTripId(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId) {
        List<TripLogs> logs = tripLogsService.getPublicLogsByTripId(userId, tripId);
        return logs.isEmpty() ? Result.success("获取成功，暂无日志", logs) : Result.success("获取成功", logs);
    }
}