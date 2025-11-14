package com.bread.traveler.controller;

import com.bread.traveler.entity.Users;
import com.bread.traveler.service.UsersService;
import com.bread.traveler.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@Tag(name = "用户管理")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping("/{userId}")
    @Operation(summary = "根据用户ID查询用户")
    public Result findUserById(@PathVariable UUID userId) {
        Users user = usersService.findUserById(userId);
        return Result.success(user);
    }

    @PostMapping
    @Operation(summary = "创建用户")
    public Result createUser(String username, String preferencesText) {
        Users user = usersService.createUser(username, preferencesText);
        return Result.success(user);
    }

    @GetMapping("/username")
    @Operation(summary = "根据用户名查询用户")
    public Result getUserByUsername(String username) {
        Users user = usersService.getUserByUsername(username);
        return Result.success(user);
    }

    @PutMapping("/preferences")
    @Operation(summary = "更新用户偏好设置")
    public Result updateUserPreferences(UUID userId, String preferencesText) {
        boolean success = usersService.updateUserPreferences(userId, preferencesText);
        return success ? Result.success("更新成功") : Result.businessError("更新失败");
    }


}
