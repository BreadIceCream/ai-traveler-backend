package com.bread.traveler.service;

import com.bread.traveler.entity.Pois;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.UUID;

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
     * 使用第三方API（高德/谷歌）搜索POI
     *
     * @param city     城市名称
     * @param keywords 搜索的poi关键词
     * @return 搜索的POI信息
     */
    List<Pois> searchPoiFromExternalApi(String city, String keywords);

    /**
     * 异步嵌入POI信息并保存到数据库
     * @param poisList POI列表
     */
    void asyncEmbeddingAndSave(List<Pois> poisList);

    /**
     * 语义搜索POI，用于灵感激发模式（F-3.5.1）
     * @param queryText 查询文本
     * @return 匹配的POI列表
     */
    List<Pois> semanticSearchPois(String queryText);

}
