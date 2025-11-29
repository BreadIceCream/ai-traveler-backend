package com.bread.traveler.controller;

import com.bread.traveler.dto.TripMemberDto;
import com.bread.traveler.enums.MemberRole;
import com.bread.traveler.service.TripMembersService;
import com.bread.traveler.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "添加成员请求", description = "向旅程添加成员请求")
    public Result addMemberRequest(@RequestParam UUID tripId, @RequestParam UUID userId) {
        boolean result = tripMembersService.addMemberRequest(tripId, userId);
        return result ? Result.success("添加成员请求成功") : Result.serverError("添加成员请求失败");
    }

    @PutMapping("/handleRequest")
    @Operation(summary = "处理成员请求", description = "接受或拒绝成员的加入请求")
    public Result handleMemberRequest(@RequestParam UUID tripId, 
                                     @RequestParam UUID handleUserId, 
                                     @RequestParam UUID currentUserId, 
                                     @RequestParam Boolean accept) {
        boolean result = tripMembersService.handleMemberRequest(tripId, currentUserId, handleUserId, accept);
        return result ? Result.success("处理成员请求成功") : Result.serverError("处理成员请求失败");
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除成员", description = "从旅程中删除指定成员")
    public Result deleteMember(@RequestParam UUID tripId, 
                              @RequestParam UUID handleUserId, 
                              @RequestParam UUID currentUserId) {
        boolean result = tripMembersService.deleteMember(tripId, currentUserId, handleUserId);
        return result ? Result.success("删除成员成功") : Result.serverError("删除成员失败");
    }

//    @PutMapping("/updateRole")
//    @Operation(summary = "更新成员角色", description = "更新指定成员在旅程中的角色")
//    public Result updateMemberRole(@RequestParam UUID tripId,
//                                   @RequestParam UUID userId,
//                                  @RequestParam UUID handleUserId,
//                                  @RequestParam MemberRole newRole) {
//        boolean result = tripMembersService.updateMemberRole(tripId, userId, handleUserId, newRole);
//        return result ? Result.success("更新成员角色成功") : Result.serverError("更新成员角色失败");
//    }

    @GetMapping("/list")
    @Operation(summary = "获取旅程成员列表", description = "获取指定旅程的所有成员列表，支持按审批状态筛选")
    public Result getMembers(@RequestParam UUID userId, @RequestParam UUID tripId,
                            @RequestParam(required = false) Boolean isPass) {
        List<TripMemberDto> members = tripMembersService.getMembers(userId, tripId, isPass);
        return members.isEmpty() ? Result.success("获取成功，暂无成员", members) : Result.success("获取成功", members);
    }

}