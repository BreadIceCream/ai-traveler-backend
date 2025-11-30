package com.bread.traveler.controller;

import com.bread.traveler.constants.Constant;
import com.bread.traveler.entity.Pois;
import com.bread.traveler.service.PoisService;
import com.bread.traveler.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author huang
 * @description POI兴趣点管理控制器
 * @createDate 2025-11-14 15:32:20
 */
@RestController
@RequestMapping("/pois")
@Tag(name = "POI兴趣点管理", description = "POI兴趣点的创建、查询、搜索等操作")
public class PoisController {

    @Autowired
    private PoisService poisService;

    @GetMapping("/{poiId}")
    @Operation(summary = "获取POI详细信息", description = "根据POI ID获取POI的详细信息")
    public Result getPoiById(
            @Parameter(description = "POI ID", required = true)
            @Schema(description = "POI ID", example = "123e4567-e89b-12d3-a456-426614174002")
            @PathVariable UUID poiId) {
        Pois pois = poisService.getPoiById(poiId);
        return Result.success(pois);
    }

    @GetMapping("/search/exact/api")
    @Operation(summary = "第三方API搜索POI并保存", description = "第三方API（高德/谷歌）搜索的POI")
    public Result searchPoiFromExternalApi(
            @Schema(description = "城市", example = "北京") 
            @RequestParam(required = false) String city,
            @Schema(description = "关键词", example = "故宫") 
            @RequestParam String keywords) {
        List<Pois> pois = poisService.searchPoiFromExternalApiAndSaveUpdate(city, keywords);
        return Result.success(pois);
    }

    @GetMapping("/search/exact/db")
    @Operation(summary = "数据库搜索POI", description = "数据库精确搜索的POI")
    public Result searchPoiFromDb(
            @Schema(description = "城市", example = "北京") 
            @RequestParam(required = false) String city,
            @Schema(description = "关键词", example = "故宫") 
            @RequestParam String keywords) {
        List<Pois> pois = poisService.searchPoiFromDb(city, keywords);
        return Result.success(pois);
    }

    @GetMapping("/search/semantic")
    @Operation(summary = "语义搜索POI", description = "使用语义搜索的POI")
    public Result semanticSearchPois(
            @Schema(description = "查询文本", example = "适合亲子游的景点") 
            @RequestParam String queryText,
            @Schema(description = "城市", example = "北京") 
            @RequestParam(required = false) String city,
            @Schema(description = "返回数量", example = "10") 
            @RequestParam(defaultValue = Constant.POIS_SEMANTIC_SEARCH_TOP_K) int topK) {
        List<Pois> pois = poisService.semanticSearchPois(queryText, city, topK);
        return Result.success(pois);
    }



}