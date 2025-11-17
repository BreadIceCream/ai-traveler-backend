package com.bread.traveler.controller;

import com.bread.traveler.constants.Constant;
import com.bread.traveler.entity.Users;
import com.bread.traveler.service.UsersService;
import com.bread.traveler.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "根据用户ID查询用户")
    public Result findUserById(@PathVariable UUID userId) {
        Users user = usersService.findUserById(userId);
        return Result.success(user);
    }

    @GetMapping("/username")
    @Operation(summary = "根据用户名查询用户")
    public Result getUserByUsername(String username) {
        Users user = usersService.getUserByUsername(username);
        return Result.success(user);
    }

    @PostMapping
    @Operation(summary = "创建用户")
    public Result createUser(String username, String preferencesText) {
        Users user = usersService.createUser(username, preferencesText);
        return Result.success(user);
    }

    @PutMapping("/preferences")
    @Operation(summary = "更新用户偏好设置")
    public Result updateUserPreferences(UUID userId, String preferencesText) {
        Users users = usersService.updateUserPreferences(userId, preferencesText);
        return Result.success("更新成功", users);
    }

    @GetMapping("/similar")
    @Operation(summary = "查找兴趣相似用户")
    public Result findSimilarUsers(@RequestParam UUID userId,
                                   @RequestParam(defaultValue = Constant.USERS_FIND_SIMILAR_LIMIT) int limit){
        List<Users> similarUsers = usersService.findSimilarUsers(userId, limit);
        return similarUsers.isEmpty() ? Result.success(Constant.USERS_FIND_SIMILAR_NO_RESULT, similarUsers) : Result.success(similarUsers);
    }


}
