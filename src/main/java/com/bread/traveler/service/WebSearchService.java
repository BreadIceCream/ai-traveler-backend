package com.bread.traveler.service;

import com.bread.traveler.entity.NonPoiItem;
import com.bread.traveler.entity.Pois;
import com.bread.traveler.entity.WebPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bread.traveler.enums.NonPoiType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
* @author huang
* @description 针对表【web_page】的数据库操作Service
* @createDate 2025-11-18 18:14:35
*/
public interface WebSearchService extends IService<WebPage> {

    String SEARCH_URL = "https://api.bocha.cn/v1/web-search";

    @Builder
    @Data
    class WebSearchParam {
        // 搜索关键字
        private String query;
        // 搜索指定时间范围内的网页
        private String freshness;
        // 是否显示文本摘要
        private Boolean summary;
        // 指定搜索的网站范围。多个域名用|或,分隔
        private String include;
        // 排除搜索的网站范围。
        private String exclude;
        // 指定返回的结果数量，1-50
        private Integer count;
    }

    enum Freshness {
        noLimit,
        oneDay,
        oneWeek,
        oneMonth,
        oneYear
    }

    @Data
    @AllArgsConstructor
    class WebSearchResults implements Serializable {
        private String originalQuery;
        private Integer resultCount;
        private List<WebPage> webPages;
    }

    @Data
    class ExtractionIntermediateResultDTO {
        // 对应 JSON 中的 "Pois"
        private List<ExtractedPoiDTO> pois;
        // 对应 JSON 中的 "NonPois"
        private List<ExtractedNonPoiDTO> nonPois;
    }

    @Data
    class ExtractedPoiDTO {
        // 景点名称
        private String name;
        private String city;
        private String description;
    }

    @Data
    class ExtractedNonPoiDTO {
        private NonPoiType type;
        private String title;
        private String description;
        private String city;
        private String activityTime;
        private String estimatedAddress;
        private String extraInfo;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class ExtractResult {
        String message;
        List<Pois> pois;
        List<NonPoiItem> nonPois;
    }

    /**
     * 搜索网页
     *
     * @param conversationId
     * @param param
     * @return 搜索结果，包含原始查询、结果数量和网页列表
     * @throws IOException
     */
    WebSearchResults webSearch(UUID conversationId, WebSearchParam param) throws IOException;

    /**
     * 从网页中提取信息,生成POI或NonPoiItem
     *
     * @param userId
     * @param city      城市。但会优先使用提取后得到的城市
     * @param webPageId
     * @return 提取的结果，包含POI和NonPoiItem的集合
     */
    ExtractResult extractItemsFromWebPageAndSave(UUID userId, String city, UUID webPageId);

}
