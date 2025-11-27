package com.bread.traveler.controller;

import com.bread.traveler.dto.EntireWishlistItem;
import com.bread.traveler.entity.WishlistItems;
import com.bread.traveler.service.WishlistItemsService;
import com.bread.traveler.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/wishlistItems")
@Tag(name = "心愿单服务")
public class WishlistItemsController {

    @Autowired
    private WishlistItemsService wishlistItemsService;

    @PostMapping("/add")
    @Operation(summary = "添加心愿单item")
    public Result addToWishList(@RequestParam UUID tripId, @RequestParam UUID entityId, @RequestParam boolean isPoi) {
        boolean result = wishlistItemsService.addToWishList(tripId, entityId, isPoi);
        return result ? Result.success("添加成功") : Result.serverError("添加失败");
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除心愿单item")
    public Result removeFromWishList(@RequestParam List<UUID> itemIds) {
        boolean result = wishlistItemsService.removeFromWishList(itemIds);
        return result ? Result.success("删除成功") : Result.serverError("删除失败");
    }

    @GetMapping("/list")
    @Operation(summary = "获取某个行程下的心愿单item列表", description = "心愿单列表，包括每个item对应的entity信息。按照添加的顺序倒序排序")
    public Result listByTripId(@RequestParam UUID tripId) {
        List<EntireWishlistItem> items = wishlistItemsService.listEntireByTripId(tripId);
        return items.isEmpty() ? Result.success("获取成功，心愿单为空", items) : Result.success("获取成功", items);
    }

}
