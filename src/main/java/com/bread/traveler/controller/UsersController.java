package com.bread.traveler.controller;

import com.bread.traveler.constants.Constant;
import com.bread.traveler.dto.UserDto;
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
    
    @GetMapping("/me")
    @Operation(summary = "获取当前用户信息", description = "获取当前用户信息")
    public Result getCurrentUserInfo(@RequestAttribute("userId") UUID userId) {
        Users user = usersService.findUserById(userId);
        user.setPassword(null);
        return Result.success(user);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "根据用户ID查询用户", description = "根据用户ID查询用户")
    public Result findUserById(@Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID userId) {
        Users user = usersService.findUserById(userId);
        user.setPassword(null);
        return Result.success(user);
    }

    @GetMapping("/username")
    @Operation(summary = "根据用户名查询用户", description = "根据用户名查询用户")
    public Result getUserByUsername(@Schema(description = "用户名", example = "testuser") @RequestParam String username) {
        Users user = usersService.getUserByUsername(username);
        user.setPassword(null);
        return Result.success(user);
    }

    @PostMapping("/register")
    @Operation(summary = "注册用户", description = "注册用户")
    public Result createUser(@RequestBody UserDto userDto) {
        usersService.createUser(userDto.getUsername(), userDto.getPassword(), userDto.getPreferencesText());
        return Result.success("注册成功", null);
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录")
    public Result login(@RequestBody UserDto userDto){
        String token = usersService.login(userDto.getUsername(), userDto.getPassword());
        return Result.success("登录成功", token);
    }

    @PutMapping("/preferences")
    @Operation(summary = "更新用户偏好设置", description = "更新用户偏好设置")
    public Result updateUserPreferences(
            @Schema(description = "用户ID") @RequestAttribute("userId") UUID userId,
            @Schema(description = "偏好文本", example = "喜欢自然风光和户外活动") @RequestParam String preferencesText) {
        Users users = usersService.updateUserPreferences(userId, preferencesText);
        return Result.success("更新成功", users);
    }

    @GetMapping("/similar")
    @Operation(summary = "查找兴趣相似用户", description = "查找兴趣相似用户")
    public Result findSimilarUsers(
            @Schema(description = "用户ID") @RequestAttribute("userId") UUID userId,
            @Schema(description = "限制数量", example = "10") @RequestParam(defaultValue = Constant.USERS_FIND_SIMILAR_LIMIT) int limit) {
        List<Users> similarUsers = usersService.findSimilarUsers(userId, limit);
        return similarUsers.isEmpty() ? Result.success(Constant.USERS_FIND_SIMILAR_NO_RESULT, similarUsers)
                : Result.success(similarUsers);
    }

}
