package com.bread.traveler.controller;

import com.bread.traveler.constants.Constant;
import com.bread.traveler.entity.Users;
import com.bread.traveler.service.UsersService;
import com.bread.traveler.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@Tag(name = "用户管理")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping("/{userId}")
    @Operation(summary = "根据用户ID查询用户", description = "根据用户ID查询用户\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": {\n    \"userId\": \"123e4567-e89b-12d3-a456-426614174000\",\n    \"username\": \"testuser\",\n    \"preferencesText\": \"喜欢历史文化和美食\",\n    \"createdAt\": \"2023-10-01T12:00:00+08:00\"\n  }\n}\n```")
    public Result findUserById(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID userId) {
        Users user = usersService.findUserById(userId);
        return Result.success(user);
    }

    @GetMapping("/username")
    @Operation(summary = "根据用户名查询用户", description = "根据用户名查询用户\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": {\n    \"userId\": \"123e4567-e89b-12d3-a456-426614174000\",\n    \"username\": \"testuser\",\n    \"preferencesText\": \"喜欢历史文化和美食\",\n    \"createdAt\": \"2023-10-01T12:00:00+08:00\"\n  }\n}\n```")
    public Result getUserByUsername(
            @Schema(description = "用户名", example = "testuser") @RequestParam String username) {
        Users user = usersService.getUserByUsername(username);
        return Result.success(user);
    }

    @PostMapping
    @Operation(summary = "创建用户", description = "创建用户\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": {\n    \"userId\": \"123e4567-e89b-12d3-a456-426614174000\",\n    \"username\": \"testuser\",\n    \"preferencesText\": \"喜欢历史文化和美食\",\n    \"createdAt\": \"2023-10-01T12:00:00+08:00\"\n  }\n}\n```")
    public Result createUser(
            @Schema(description = "用户名", example = "testuser") @RequestParam String username,
            @Schema(description = "偏好文本", example = "喜欢历史文化和美食") @RequestParam String preferencesText) {
        Users user = usersService.createUser(username, preferencesText);
        return Result.success(user);
    }

    @PutMapping("/preferences")
    @Operation(summary = "更新用户偏好设置", description = "更新用户偏好设置\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": {\n    \"userId\": \"123e4567-e89b-12d3-a456-426614174000\",\n    \"username\": \"testuser\",\n    \"preferencesText\": \"喜欢自然风光和户外活动\",\n    \"createdAt\": \"2023-10-01T12:00:00+08:00\"\n  }\n}\n```")
    public Result updateUserPreferences(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "偏好文本", example = "喜欢自然风光和户外活动") @RequestParam String preferencesText) {
        Users users = usersService.updateUserPreferences(userId, preferencesText);
        return Result.success("更新成功", users);
    }

    @GetMapping("/similar")
    @Operation(summary = "查找兴趣相似用户", description = "查找兴趣相似用户\n\nResponse Example:\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": [\n    {\n      \"userId\": \"123e4567-e89b-12d3-a456-426614174000\",\n      \"username\": \"testuser\",\n      \"preferencesText\": \"喜欢历史文化和美食\",\n      \"createdAt\": \"2023-10-01T12:00:00+08:00\"\n    }\n  ]\n}\n```")
    public Result findSimilarUsers(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID userId,
            @Schema(description = "限制数量", example = "10") @RequestParam(defaultValue = Constant.USERS_FIND_SIMILAR_LIMIT) int limit) {
        List<Users> similarUsers = usersService.findSimilarUsers(userId, limit);
        return similarUsers.isEmpty() ? Result.success(Constant.USERS_FIND_SIMILAR_NO_RESULT, similarUsers)
                : Result.success(similarUsers);
    }

}
