package com.bread.traveler.controller;

import com.bread.traveler.dto.TripMemberDto;
import com.bread.traveler.service.TripMembersService;
import com.bread.traveler.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tripMembers")
@Tag(name = "旅程成员管理")
public class TripMembersController {

    @Autowired
    private TripMembersService tripMembersService;

    @PostMapping("/addRequest")
    @Operation(summary = "申请加入旅程", description = "向旅程添加成员请求")
    public Result addMemberRequest(
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId,
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId) {
        boolean result = tripMembersService.addMemberRequest(tripId, userId);
        return result ? Result.success("申请成功", null) : Result.serverError("申请失败");
    }

    @PutMapping("/handleRequest")
    @Operation(summary = "处理成员请求", description = "接受或拒绝成员的加入请求")
    public Result handleMemberRequest(
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId,
            @Schema(description = "处理的用户ID", example = "123e4567-e89b-12d3-a456-426614174002") @RequestParam UUID handleUserId,
            @Schema(description = "当前用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID currentUserId,
            @Schema(description = "是否接受", example = "true") @RequestParam Boolean accept) {
        boolean result = tripMembersService.handleMemberRequest(tripId, currentUserId, handleUserId, accept);
        return result ? Result.success("处理成员请求成功", null) : Result.serverError("处理成员请求失败");
    }

    @PostMapping("/invite")
    @Operation(summary = "邀请用户", description = "邀请用户加入旅程")
    public Result inviteMember(
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId,
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "被邀请用户ID列表", example = "[\"123e4567-e89b-12d3-a456-426614174003\", \"123e4567-e89b-12d3-a456-426614174004\"]") @RequestParam List<UUID> inviteUserIds) {
        boolean result = tripMembersService.inviteMembers(tripId, userId, inviteUserIds);
        return result ? Result.success("邀请成功", null) : Result.serverError("邀请失败");
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除成员", description = "从旅程中删除指定成员")
    public Result deleteMember(
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId,
            @Schema(description = "处理的用户ID", example = "123e4567-e89b-12d3-a456-426614174002") @RequestParam UUID handleUserId,
            @Schema(description = "当前用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID currentUserId) {
        boolean result = tripMembersService.deleteMember(tripId, currentUserId, handleUserId);
        return result ? Result.success("删除成员成功", null) : Result.serverError("删除成员失败");
    }

    // @PutMapping("/updateRole")
    // @Operation(summary = "更新成员角色", description = "更新指定成员在旅程中的角色")
    // public Result updateMemberRole(@RequestParam UUID tripId,
    // @RequestParam UUID userId,
    // @RequestParam UUID handleUserId,
    // @RequestParam MemberRole newRole) {
    // boolean result = tripMembersService.updateMemberRole(tripId, userId,
    // handleUserId, newRole);
    // return result ? Result.success("更新成员角色成功") : Result.serverError("更新成员角色失败");
    // }

    @GetMapping("/list")
    @Operation(summary = "获取旅程成员列表", description = "获取指定旅程的所有成员列表，支持按审批状态筛选")
    public Result getMembers(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") @RequestParam UUID tripId,
            @Schema(description = "是否通过审批", example = "true") @RequestParam(required = false) Boolean isPass) {
        List<TripMemberDto> members = tripMembersService.getTripMembers(userId, tripId, isPass);
        return members.isEmpty() ? Result.success("获取成功，暂无成员", members) : Result.success("获取成功", members);
    }

}