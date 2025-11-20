package com.bread.traveler.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bread.traveler.constants.Constant;
import com.bread.traveler.entity.NonPoiItem;
import com.bread.traveler.entity.Pois;
import com.bread.traveler.entity.WebPage;
import com.bread.traveler.service.NonPoiItemService;
import com.bread.traveler.service.PoisService;
import com.bread.traveler.service.WebSearchService;
import com.bread.traveler.mapper.WebPageMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.*;

/**
* @author huang
* @description 针对表【web_page】的数据库操作Service实现
* @createDate 2025-11-18 18:14:35
*/
@Service
@Slf4j
public class WebSearchServiceImpl extends ServiceImpl<WebPageMapper, WebPage> implements WebSearchService {

    @Value("${web-search.api-key}")
    private String webSearchApiKey;
    @Autowired
    @Qualifier("extractItemsClient")
    private ObjectProvider<ChatClient> extractItemsClientProvider;
    @Autowired
    private PoisService poisService;
    @Autowired
    private NonPoiItemService nonPoiItemService;

    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(10);

    @Override
    public WebSearchResults webSearch(UUID conversationId, WebSearchParam param) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(SEARCH_URL);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Authorization", "Bearer " + webSearchApiKey);
            httpPost.setEntity(new StringEntity(JSONUtil.toJsonStr(param), "UTF-8"));
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                HttpEntity entity = response.getEntity();
                String json = EntityUtils.toString(entity, "UTF-8");
                JSONObject resultObj = JSONUtil.parseObj(json);
                String code = resultObj.getStr("code");
                if (!"200".equals(code)) {
                    log.error("Web search failed. Code: {}, msg: {}", code, resultObj.getStr("msg"));
                    throw new RuntimeException(Constant.WEB_SEARCH_FAILED);
                }
                // 解析搜索结果，封装成WebSearchResults对象
                JSONObject data = resultObj.getJSONObject("data");
                JSONObject queryContext = data.getJSONObject("queryContext");
                JSONObject oriWebPages = data.getJSONObject("webPages");
                JSONArray oriWebPageList = oriWebPages.getJSONArray("value");
                List<WebPage> parsedWebPages = oriWebPageList.stream().map(item -> {
                    WebPage webPage = new WebPage();
                    BeanUtil.copyProperties(item, webPage,
                            CopyOptions.create().setIgnoreProperties("id").ignoreNullValue());
                    // 设置webPage Id 和 会话ID
                    webPage.setId(UUID.randomUUID());
                    webPage.setConversationId(conversationId);
                    webPage.setCreatedAt(OffsetDateTime.now(ZoneId.systemDefault()));
                    return webPage;
                }).toList();
                return new WebSearchResults(queryContext.getStr("originalQuery"), parsedWebPages.size(), parsedWebPages);
            }
        }
    }

    @Override
    // todo 添加事务
    public ExtractResult extractItemsFromWebPage(UUID userId, String city, UUID webPageId) {
        log.info("Extract items from web page: {}", webPageId);
        WebPage webPage = getById(webPageId);
        if (webPage == null){
            log.warn("Extract: Web page not found: {}", webPageId);
            return null;
        }
        // 生成prompt
        String prompt = "title:" + webPage.getName() + System.lineSeparator() + "url:" + webPage.getUrl() + System.lineSeparator() + "开始提取";
        ChatClient extractClient = extractItemsClientProvider.getObject();
        ExtractionIntermediateResultDTO intermediateResult = extractClient.prompt().user(prompt).call().entity(new ParameterizedTypeReference<>() {});
        if (intermediateResult == null){
            log.warn("Extract result is null");
            return null;
        }
        // 解析提取的结果，返回Pois集合和NonPoiItem集合。
        List<ExtractedPoiDTO> poiDtos = intermediateResult.getPois();
        List<ExtractedNonPoiDTO> nonPoiDtos = intermediateResult.getNonPois();
        //pois解析异步处理（涉及网络IO），同时保存
        Future<List<Pois>> poisParseTask = THREAD_POOL.submit(() -> poiDtos.stream().map(poiDto -> {
            // 调用高德API获取该POI的详细信息
            String poiDtoCity = poiDto.getCity();
            String queryCity = poiDtoCity == null || poiDtoCity.isEmpty() ? city : poiDtoCity;
            List<Pois> pois = poisService.searchPoiFromExternalApiAndSave(queryCity, poiDto.getName(), 1,
                    // 使用解析后的描述
                    objects -> List.of(poiDto.getDescription()));
            if (pois == null || pois.isEmpty()) {
                // external API搜索失败
                log.warn("Extract: Pois search not found: {}", poiDto.getName());
                return null;
            }
            return pois.getFirst();
        }).filter(Objects::nonNull).toList());
        // 非POI项解析同步处理，同时保存
        List<NonPoiItem> nonPoiItems = nonPoiDtos.stream().map(nonPoiDto -> {
            // 直接copy，并补充字段
            NonPoiItem nonPoiItem = BeanUtil.copyProperties(nonPoiDto, NonPoiItem.class);
            nonPoiItem.setId(UUID.randomUUID());
            nonPoiItem.setSourceUrl(webPage.getUrl());
            nonPoiItem.setCreatedAt(OffsetDateTime.now(ZoneId.systemDefault()));
            nonPoiItem.setPrivateUserId(userId);
            return nonPoiItem;
        }).toList();
        nonPoiItemService.saveBatch(nonPoiItems);
        // 等待 pois 解析完成，返回结果
        List<Pois> pois = null;
        try {
            pois = poisParseTask.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Extract: Pois parse failed", e);
            throw new RuntimeException(e);
        }
        return new ExtractResult(pois, nonPoiItems);
    }
}




