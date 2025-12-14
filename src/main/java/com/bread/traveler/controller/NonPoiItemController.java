package com.bread.traveler.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bread.traveler.dto.NonPoiItemDto;
import com.bread.traveler.entity.NonPoiItem;
import com.bread.traveler.enums.NonPoiType;
import com.bread.traveler.service.NonPoiItemService;
import com.bread.traveler.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/nonPoiItem")
@Tag(name = "非POI项管理")
public class NonPoiItemController {

    @Autowired
    private NonPoiItemService nonPoiItemService;

    @PostMapping("/create")
    @Operation(summary = "创建非POI项", description = "创建非POI项")
    public Result createNonPoiItem(
            @Schema(description = "用户ID") @RequestAttribute("userId") UUID userId,
            @Schema(description = "非POI项dto") @RequestBody NonPoiItemDto dto) {
        NonPoiItem nonPoiItem = nonPoiItemService.createNonPoiItem(userId, dto);
        return nonPoiItem != null ? Result.success("创建成功", nonPoiItem) : Result.serverError("创建失败");
    }

    @GetMapping("/page")
    @Operation(summary = "分页获取用户的非POI项列表", description = "分页获取用户的非POI项列表")
    public Result getNonPoiItemList(
            @Schema(description = "用户ID") @RequestAttribute("userId") UUID userId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) NonPoiType type) {
        Page<NonPoiItem> nonPoiItemList = nonPoiItemService.getPageByUserId(userId, pageNum, pageSize, type);
        return Result.success(nonPoiItemList);
    }

    @PutMapping("/update")
    @Operation(summary = "更新非POI项", description = "更新非POI项")
    public Result updateNonPoiItem(
            @Schema(description = "用户ID") @RequestAttribute("userId") UUID userId,
            @Schema(description = "非POI项dto") @RequestBody NonPoiItemDto dto) {
        NonPoiItem nonPoiItem = nonPoiItemService.updateNonPoiItem(userId, dto);
        return nonPoiItem != null ? Result.success("更新成功", nonPoiItem) : Result.serverError("更新失败");
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除非POI项", description = "删除非POI项")
    public Result deleteNonPoiItem(
            @Schema(description = "用户ID") @RequestAttribute("userId") UUID userId,
            @Schema(description = "非POI项ID", example = "123e4567-e89b-12d3-a456-426614174002") @RequestParam UUID nonPoiItemId) {
        boolean success = nonPoiItemService.deleteByIds(userId, List.of(nonPoiItemId));
        return success ? Result.success("删除成功", null) : Result.serverError("删除失败");
    }

}
