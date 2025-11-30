package com.bread.traveler.controller;

import com.bread.traveler.dto.TripNoteLogCreateDto;
import com.bread.traveler.entity.TripLogs;
import com.bread.traveler.enums.LogType;
import com.bread.traveler.service.TripLogsService;
import com.bread.traveler.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "创建文本日志")
    public Result createNoteLog(@RequestParam UUID userId, @RequestParam UUID tripId, @RequestBody TripNoteLogCreateDto dto) {
        TripLogs log = tripLogsService.createNoteLog(userId, tripId, dto.getContent(), dto.getIsPublic());
        return log == null ? Result.serverError("创建失败") : Result.success("创建成功", log);
    }

    @PostMapping("/image")
    @Operation(summary = "创建图片日志", description = "上传图片文件，最大为3MB")
    public Result createImgLog(@RequestParam UUID userId, @RequestParam UUID tripId,
                               @RequestParam("imgFile") MultipartFile imgFile, @RequestParam(required = false) Boolean isPublic) {
        TripLogs log = tripLogsService.createImgLog(userId, tripId, imgFile, isPublic);
        return log == null ? Result.serverError("创建失败") : Result.success("创建成功", log);
    }

    @PutMapping("/visibility")
    @Operation(summary = "修改日志可见性")
    public Result updateLogVisibility(@RequestParam UUID userId, @RequestParam UUID logId, @RequestParam Boolean isPublic) {
        boolean result = tripLogsService.changeLogVisibility(userId, logId, isPublic);
        return result ? Result.success("修改成功") : Result.serverError("修改失败");
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除日志")
    public Result deleteLog(@RequestParam UUID userId, @RequestParam UUID logId) {
        boolean result = tripLogsService.deleteLog(userId, logId);
        return result ? Result.success("删除成功") : Result.serverError("删除失败");
    }

    @GetMapping("/trip")
    @Operation(summary = "获取当前用户某个旅程所有日志")
    public Result getLogsByTripId(@RequestParam UUID userId, @RequestParam UUID tripId) {
        List<TripLogs> logs = tripLogsService.getLogsOfUserByTripId(userId, tripId);
        return logs.isEmpty() ? Result.success("获取成功，暂无日志", logs) : Result.success("获取成功", logs);
    }

    @GetMapping("/trip/type")
    @Operation(summary = "获取当前用户某个旅程指定类型日志")
    public Result getLogsByTripIdAndType(@RequestParam UUID userId, @RequestParam UUID tripId, @RequestParam LogType type) {
        List<TripLogs> logs = tripLogsService.getLogsOfUserByTripIdAndType(userId, tripId, type);
        return logs.isEmpty() ? Result.success("获取成功，暂无日志", logs) : Result.success("获取成功", logs);
    }

    @GetMapping("/trip/public")
    @Operation(summary = "获取旅程公开日志")
    public Result getPublicLogsByTripId(@RequestParam UUID userId, @RequestParam UUID tripId) {
        List<TripLogs> logs = tripLogsService.getPublicLogsByTripId(userId, tripId);
        return logs.isEmpty() ? Result.success("获取成功，暂无日志", logs) : Result.success("获取成功", logs);
    }
}