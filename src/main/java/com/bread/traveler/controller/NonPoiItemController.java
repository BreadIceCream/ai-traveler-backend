package com.bread.traveler.controller;

import com.bread.traveler.dto.NonPoiItemDto;
import com.bread.traveler.entity.NonPoiItem;
import com.bread.traveler.service.NonPoiItemService;
import com.bread.traveler.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "创建非POI项")
    public Result createNonPoiItem(@RequestParam UUID userId, @RequestBody NonPoiItemDto dto){
        NonPoiItem nonPoiItem = nonPoiItemService.createNonPoiItem(userId, dto);
        return nonPoiItem != null ? Result.success("创建成功", nonPoiItem) : Result.serverError("创建失败");
    }

    @GetMapping("/list")
    @Operation(summary = "获取用户的非POI项列表")
    public Result getNonPoiItemList(@RequestParam UUID userId){
        List<NonPoiItem> nonPoiItemList = nonPoiItemService.getByUserId(userId);
        return Result.success(nonPoiItemList);
    }


    @PutMapping("/update")
    @Operation(summary = "更新非POI项")
    public Result updateNonPoiItem(@RequestParam UUID userId, @RequestBody NonPoiItemDto dto){
        NonPoiItem nonPoiItem = nonPoiItemService.updateNonPoiItem(userId, dto);
        return nonPoiItem != null ? Result.success("更新成功", nonPoiItem) : Result.serverError("更新失败");
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除非POI项")
    public Result deleteNonPoiItem(@RequestParam UUID userId, @RequestParam UUID nonPoiItemId){
        boolean success = nonPoiItemService.deleteByIds(userId, List.of(nonPoiItemId));
        return success ? Result.success("删除成功") : Result.serverError("删除失败");
    }

}
