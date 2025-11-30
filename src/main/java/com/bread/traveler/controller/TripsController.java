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
    @Operation(summary = "创建旅程")
    public Result createTrip(@RequestParam UUID userId, @RequestBody TripDto dto) {
        Trips trip = tripsService.createTrip(userId, dto);
        return trip == null ? Result.serverError("创建失败") : Result.success("创建成功", trip);
    }

    @PutMapping("/update")
    @Operation(summary = "更新旅程信息")
    public Result updateTripInfo(@RequestParam UUID userId, @RequestParam UUID tripId, @RequestBody TripDto dto) {
        Trips trip = tripsService.updateTripInfo(userId, tripId, dto);
        return trip == null ? Result.serverError("更新失败") : Result.success("更新成功", trip);
    }

    @PutMapping("/visibility")
    @Operation(summary = "更新旅程可见性")
    public Result updateTripVisibility(@RequestParam UUID userId, @RequestParam UUID tripId, @RequestParam Boolean isPrivate) {
        boolean result = tripsService.changeVisibility(userId, tripId, isPrivate);
        return result ? Result.success("更新成功") : Result.serverError("更新失败");
    }

    @PutMapping("/status")
    @Operation(summary = "更新旅程状态")
    public Result updateTripStatus(@RequestParam UUID userId, @RequestParam UUID tripId, @RequestParam TripStatus newStatus) {
        boolean result = tripsService.changeStatus(userId, tripId, newStatus);
        return result ? Result.success("更新成功") : Result.serverError("更新失败");
    }

    @GetMapping("/user")
    @Operation(summary = "获取用户所有旅程")
    public Result getAllTripsOfUser(@RequestParam UUID userId) {
        List<TripWithMemberInfoDto> trips = tripsService.getAllTripsOfUser(userId);
        return trips.isEmpty() ? Result.success("获取成功，暂无旅程", trips) : Result.success("获取成功", trips);
    }

    @GetMapping("/public")
    @Operation(summary = "分页获取公开旅程")
    public Result getPublicTrips(@RequestParam(required = false) String destinationCity,
                                 @RequestParam(required = false) LocalDate startDate,
                                 @RequestParam(required = false) LocalDate endDate,
                                 @RequestParam(defaultValue = "1") Integer pageNum,
                                 @RequestParam(defaultValue = "10") Integer pageSize){
        Page<Trips> publicTrips = tripsService.getPublicTrips(destinationCity, startDate, endDate, pageNum, pageSize);
        return Result.success("获取成功", publicTrips);
    }

    @GetMapping("/detail")
    @Operation(summary = "获取完整旅程信息")
    public Result getEntireTrip(@RequestParam UUID userId, @RequestParam UUID tripId) {
        EntireTrip entireTrip = tripsService.getEntireTrip(userId, tripId);
        return Result.success("获取成功", entireTrip);
    }

    @PostMapping("/ai-generate")
    @Operation(summary = "AI智能生成完整旅程")
    public Result aiGenerateEntireTrip(@RequestParam UUID userId, @RequestParam UUID tripId) {
        EntireTrip result = tripsService.aiGenerateEntireTripPlan(userId, tripId);
        return result == null ? Result.serverError("AI规划失败") : Result.success("AI规划成功", result);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除旅程，级联删除所有日程和item")
    public Result deleteTrip(@RequestParam UUID userId, @RequestParam UUID tripId) {
        boolean result = tripsService.deleteTrip(userId, tripId);
        return result ? Result.success("删除成功") : Result.serverError("删除失败");
    }
}