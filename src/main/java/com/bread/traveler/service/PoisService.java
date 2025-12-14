package com.bread.traveler.service;

import cn.hutool.json.JSONArray;
import com.bread.traveler.entity.Pois;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * @author huang
 * @description 针对表【pois】的数据库操作Service
 * @createDate 2025-11-14 12:09:43
 */
public interface PoisService extends IService<Pois> {

    /**
     * 根据ID获取POI详细信息
     * @param poiId POI ID
     * @return POI详细信息
     */
    Pois getPoiById(UUID poiId);

    /**
     * 从数据库中批量获取POI信息
     * @param city      城市名称
     * @param keywords  搜索的poi关键词
     * @return 匹配的POI列表
     */
    List<Pois> searchPoiFromDb(String city, String keywords);

    /**
     * 使用第三方API（高德/谷歌）搜索POI，并保存至数据库或更新数据库
     *
     * @param city                 城市名称
     * @param keywords             搜索的POI关键词
     * @param searchNumber         搜索的POI数量
     * @param generateDescriptions 用于生成POI描述, 输入为高德返回的POI数据数组，输出为对应的POI描述集合
     * @return 搜索的POI信息
     */
    List<Pois> searchPoiFromExternalApiAndSaveUpdate(@Nullable String city, String keywords,
                                                     Integer searchNumber,
                                                     Function<JSONArray, List<String>> generateDescriptions);

    /**
     * 重载方法，使用第三方API（高德/谷歌）搜索POI并保存至数据库，默认使用LLM生成POI描述
     * @param city
     * @param keywords
     * @return
     */
    List<Pois> searchPoiFromExternalApiAndSaveUpdate(@Nullable String city, String keywords);

    /**
     * 异步嵌入POI信息并保存到数据库
     * @param poisList POI列表
     */
    void asyncEmbeddingAndSaveUpdate(List<Pois> poisList);

    /**
     * 语义搜索POI，用于灵感激发模式
     * @param queryText 查询文本
     * @param city 城市名称
     * @param topK 要查询的POI数量
     * @return 匹配的POI列表, 按照分数降序排列
     */
    List<Pois> semanticSearchPois(String queryText, String city, int topK);

}
