package com.bread.traveler.controller;

import com.bread.traveler.dto.TripExpenseCreateUpdateDto;
import com.bread.traveler.entity.TripExpenses;
import com.bread.traveler.enums.ExpenseType;
import com.bread.traveler.service.TripExpensesService;
import com.bread.traveler.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/trip-expenses")
@Tag(name = "支出管理")
public class TripExpensesController {

    @Autowired
    private TripExpensesService expensesService;

    @PostMapping("/add")
    @Operation(summary = "添加单笔支出")
    public Result addExpense(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") 
            @RequestParam UUID userId, 
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") 
            @RequestParam UUID tripId, 
            @RequestBody TripExpenseCreateUpdateDto dto) {
        TripExpenses expense = expensesService.addExpense(userId, tripId, dto);
        return expense == null ? Result.serverError("添加失败") : Result.success("添加成功", expense);
    }

    @PostMapping("/batch-add")
    @Operation(summary = "批量添加支出记录")
    public Result batchAddExpenses(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") 
            @RequestParam UUID userId,
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") 
            @RequestParam UUID tripId,
            @RequestBody List<TripExpenseCreateUpdateDto> dtos) {
        boolean result = expensesService.batchAddExpenses(userId, tripId, dtos);
        return result ? Result.success("批量添加成功") : Result.serverError("批量添加失败");
    }

    @PutMapping("/update")
    @Operation(summary = "更新支出记录")
    public Result updateExpense(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") 
            @RequestParam UUID userId,
            @Schema(description = "支出ID", example = "123e4567-e89b-12d3-a456-426614174002") 
            @RequestParam UUID expenseId,
            @RequestBody TripExpenseCreateUpdateDto dto) {
        TripExpenses expense = expensesService.updateExpense(userId, expenseId, dto);
        return expense == null ? Result.serverError("更新失败") : Result.success("更新成功", expense);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除支出记录")
    public Result deleteExpense(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") 
            @RequestParam UUID userId, 
            @Schema(description = "支出ID", example = "123e4567-e89b-12d3-a456-426614174002") 
            @RequestParam UUID expenseId) {
        boolean result = expensesService.deleteExpense(userId, expenseId);
        return result ? Result.success("删除成功") : Result.serverError("删除失败");
    }

    @GetMapping("/detail")
    @Operation(summary = "获取单笔支出详情")
    public Result getExpenseById(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") 
            @RequestParam UUID userId, 
            @Schema(description = "支出ID", example = "123e4567-e89b-12d3-a456-426614174002") 
            @RequestParam UUID expenseId) {
        TripExpenses expense = expensesService.getExpenseById(userId, expenseId);
        return expense == null ? Result.serverError("获取失败") : Result.success("获取成功", expense);
    }

    @GetMapping("/list")
    @Operation(summary = "获取指定旅程的所有支出记录")
    public Result getExpensesByTripId(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") 
            @RequestParam UUID userId, 
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") 
            @RequestParam UUID tripId) {
        List<TripExpenses> expenses = expensesService.getExpensesByTripId(userId, tripId);
        return expenses.isEmpty() ? Result.success("获取成功，暂无支出记录", expenses) : Result.success("获取成功", expenses);
    }

    @GetMapping("/statistics/total")
    @Operation(summary = "获取指定旅程的总支出统计信息")
    public Result getTotalExpenseStatisticsByTripId(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") 
            @RequestParam UUID userId, 
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") 
            @RequestParam UUID tripId) {
        DoubleSummaryStatistics statistics = expensesService.getTotalExpenseStatisticsByTripId(userId, tripId);
        return Result.success("获取成功", statistics);
    }

    @GetMapping("/statistics/category")
    @Operation(summary = "获取指定旅程的各类支出统计信息")
    public Result getCategoryExpenseStatisticsByTripId(
            @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000") 
            @RequestParam UUID userId, 
            @Schema(description = "旅程ID", example = "123e4567-e89b-12d3-a456-426614174001") 
            @RequestParam UUID tripId) {
        Map<ExpenseType, DoubleSummaryStatistics> statistics = expensesService.getCategoryExpenseStatisticsByTripId(userId, tripId);
        return Result.success("获取成功", statistics);
    }
}