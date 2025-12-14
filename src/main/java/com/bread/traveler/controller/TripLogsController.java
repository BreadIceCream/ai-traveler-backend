package com.bread.traveler.controller;

import com.bread.traveler.dto.TripLogsDto;
import com.bread.traveler.dto.TripNoteLogCreateDto;
import com.bread.traveler.entity.TripLogs;
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

    @PostMapping("/create")
    @Operation(summary = "创建日志", description = "创建日志，支持上传多张照片和添加文本。单个照片不超过3MB，全部照片不超过30MB")
    public Result createNoteLog(
            @Schema(description = "用户ID") @RequestAttribute("userId") UUID userId,
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId,
            @RequestPart(required = false) List<MultipartFile> imgFiles,
            @RequestPart TripNoteLogCreateDto tripNoteLogCreateDto) {
        String msg = tripLogsService.createLog(userId, tripId, tripNoteLogCreateDto.getContent(), imgFiles, tripNoteLogCreateDto.getIsPublic());
        return Result.success(msg);
    }

    @PutMapping("/visibility")
    @Operation(summary = "修改日志可见性", description = "修改日志可见性")
    public Result updateLogVisibility(
            @Schema(description = "用户ID") @RequestAttribute("userId") UUID userId,
            @Schema(description = "日志ID", example = "123e4567-e89b-12d3-a456-426614174002") @RequestParam UUID logId,
            @Schema(description = "是否公开", example = "false") @RequestParam Boolean isPublic) {
        boolean result = tripLogsService.changeLogVisibility(userId, logId, isPublic);
        return result ? Result.success("修改成功", null) : Result.serverError("修改失败");
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除日志", description = "删除日志")
    public Result deleteLog(
            @Schema(description = "用户ID") @RequestAttribute("userId") UUID userId,
            @Schema(description = "日志ID", example = "123e4567-e89b-12d3-a456-426614174002") @RequestParam UUID logId) {
        boolean result = tripLogsService.deleteLog(userId, logId);
        return result ? Result.success("删除成功", null) : Result.serverError("删除失败");
    }

    @GetMapping("/trip")
    @Operation(summary = "获取当前用户某个旅程所有日志", description = "获取当前用户某个旅程所有日志")
    public Result getLogsByTripId(
            @Schema(description = "用户ID") @RequestAttribute("userId") UUID userId,
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId) {
        List<TripLogs> logs = tripLogsService.getLogsOfUserByTripId(userId, tripId);
        return logs.isEmpty() ? Result.success("获取成功，暂无日志", logs) : Result.success("获取成功", logs);
    }

    @GetMapping("/trip/public")
    @Operation(summary = "获取旅程公开日志", description = "获取旅程公开日志")
    public Result getPublicLogsByTripId(
            @Schema(description = "用户ID") @RequestAttribute("userId") UUID userId,
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId) {
        List<TripLogsDto> logs = tripLogsService.getPublicLogsByTripId(userId, tripId);
        return logs.isEmpty() ? Result.success("获取成功，暂无日志", logs) : Result.success("获取成功", logs);
    }
}