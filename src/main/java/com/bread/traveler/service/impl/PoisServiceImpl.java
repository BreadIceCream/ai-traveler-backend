package com.bread.traveler.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bread.traveler.constants.Constant;
import com.bread.traveler.entity.Pois;
import com.bread.traveler.exception.BusinessException;
import com.bread.traveler.service.PoisService;
import com.bread.traveler.mapper.PoisMapper;
import com.bread.traveler.utils.GaoDeUtils;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author huang
 * @description 针对表【pois】的数据库操作Service实现
 * @createDate 2025-11-14 12:09:43
 */
@Service
@Slf4j
public class PoisServiceImpl extends ServiceImpl<PoisMapper, Pois> implements PoisService {

    @Autowired
    private VectorStore vectorStore;
    @Autowired
    private GaoDeUtils gaoDeUtils;
    @Autowired
    @Qualifier("miniTaskClient")
    private ObjectProvider<ChatClient> miniTaskClientProvider;

    @Autowired
    @Lazy
    private PoisService selfProxy;

    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(10);

    @Override
    public Pois getPoiById(UUID poiId) {
        log.info("Getting POI by ID: {}", poiId);
        Pois pois = getById(poiId);
        if (pois == null) {
            throw new BusinessException(Constant.POIS_NOT_FOUND);
        }
        return pois;
    }

    @Override
    public List<Pois> searchPoiFromDb(String city, String keywords) {
        log.info("Searching POIs from database: city={}, keywords={}", city, keywords);
        LambdaQueryChainWrapper<Pois> wrapper = lambdaQuery().like(Pois::getName, keywords);
        if (city != null && !city.trim().isEmpty()) {
            wrapper.like(Pois::getCity, city);
        }
        List<Pois> results = wrapper.list();
        if (results == null || results.isEmpty()){
            log.warn("No results found in DB for the search query: city={}, keywords={}", city, keywords);
        }
        return results;
    }

    @Override
    public List<Pois> searchPoiFromExternalApiAndSaveUpdate(@Nullable String city,
                                                            String keywords,
                                                            Integer searchNumber,
                                                            Function<JSONArray, List<String>> generateDescriptions) {
        log.info("Searching POIs from external API: city={}, keywords={}", city, keywords);
        if (keywords == null || keywords.trim().isEmpty()) {
            throw new RuntimeException(Constant.POIS_SEARCH_INVALID_PARAM);
        }
        GaoDeUtils.SearchPoiParam.SearchPoiParamBuilder paramBuilder = GaoDeUtils.SearchPoiParam.builder()
                .keywords(keywords).showFields(List.of(Constant.SHOW_FIELD.BUSINESS, Constant.SHOW_FIELD.PHOTOS))
                .pageSize(searchNumber);
        if (city != null && !city.trim().isEmpty()) {
            // 如果有城市限制，则使用城市限制
            paramBuilder.city(city).cityLimit(true);
        }
        GaoDeUtils.SearchPoiParam param = paramBuilder.build();
        JSONObject results = null;
        try {
            results = gaoDeUtils.searchPoi(param);
        } catch (IOException e) {
            log.error("Failed to search POIs from external API: {}", e.getMessage());
            throw new RuntimeException(Constant.POIS_SEARCH_FAILED);
        }
        if (!Objects.equals(results.getStr("status"), "1")) {
            // 如果status不为1，搜索失败
            throw new BusinessException(Constant.POIS_SEARCH_FAILED + ", " + results.getStr("info"));
        }
        JSONArray pois = results.getJSONArray("pois");
        // 解析POI数据，生成description描述信息，封装成poi对象。使用传入的generateDescriptions方法
        Future<List<String>> descriptionsTask = THREAD_POOL.submit(() -> generateDescriptions.apply(pois));
        List<Pois> parsedPois = pois.stream().map(poi -> {
            JSONObject poiJson = (JSONObject) poi;
            return parsePoiFromGaode(poiJson);
        }).toList();
        try {
            List<String> descriptions = descriptionsTask.get();
            for (int i = 0; i < descriptions.size(); i++) {
                parsedPois.get(i).setDescription(descriptions.get(i));
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(Constant.POIS_GENERATE_DESCRIPTION_FAILED);
        }
        if (parsedPois.isEmpty()) {
            throw new BusinessException(Constant.POIS_SEARCH_NO_RESULT);
        }
        //异步处理embedding向量，写入数据库 todo采用消息队列
        THREAD_POOL.submit(() -> selfProxy.asyncEmbeddingAndSaveUpdate(parsedPois));
        return parsedPois;
    }

    @Override
    public List<Pois> searchPoiFromExternalApiAndSaveUpdate(@Nullable String city, String keywords) {
        return this.searchPoiFromExternalApiAndSaveUpdate(city, keywords,
                Constant.POIS_SEARCH_EXTERNAL_API_RETURN_NUMBER,
                this::defaultGenerateDescriptions);
    }

    /**
     * 生成POI描述
     * @param pois 高德返回的POI数组（JSONArray格式）
     * @return
     */
    private List<String> defaultGenerateDescriptions(JSONArray pois) {
        // 获取所有POI名称
        List<String> poisName = pois.stream().map(poi -> {
            JSONObject poiJson = (JSONObject) poi;
            return poiJson.getStr("name");
        }).toList();
        log.info("Default generate descriptions for POIs: {}", poisName);
        PromptTemplate promptTemplate = new PromptTemplate(new ClassPathResource("prompts/DefaultGenerateDescriptionTemplate.md"));
        Prompt prompt = promptTemplate.create(Map.of("poisName", poisName.toString()));
        List<String> descriptions = miniTaskClientProvider.getObject().prompt(prompt)
                .call().entity(new ParameterizedTypeReference<>() {
                });
        log.info("Descriptions generated success!");
        return descriptions;
    }

    /**
     * 解析POI数据，转为POI对象
     *
     * @param poiJson
     * @return
     */
    private Pois parsePoiFromGaode(JSONObject poiJson) {
        // 设置基础信息
        String[] location = poiJson.getStr("location").split(",");
        Pois.PoisBuilder poisBuilder = Pois.builder()
                .poiId(UUID.randomUUID())
                .externalApiId(poiJson.getStr("id"))
                .name(poiJson.getStr("name"))
                .type(poiJson.getStr("type"))
                .city(poiJson.getStr("cityname"))
                .address(poiJson.getStr("adname") + poiJson.getStr("address"))
                .longitude(new BigDecimal(location[0]))
                .latitude(new BigDecimal(location[1]))
                .createdAt(OffsetDateTime.now(ZoneId.systemDefault()));
        // 设置business信息
        if (poiJson.containsKey("business")) {
            JSONObject business = poiJson.getJSONObject("business");
            poisBuilder
                    .openingHours(business.getStr("opentime_week", ""))
                    .phone(business.getStr("tel", ""))
                    .rating(business.getStr("rating", ""))
                    .avgCost(business.getStr("cost", ""));
        }
        // 设置图片信息
        if (poiJson.containsKey("photos")) {
            JSONArray photos = poiJson.getJSONArray("photos");
            List<String> photoUrls = new ArrayList<>();
            for (int i = 0; i < photos.size(); i++) {
                photoUrls.add(photos.getJSONObject(i).getStr("url"));
            }
            poisBuilder.photos(photoUrls);
        }
        return poisBuilder.build();
    }


    /**
     * 异步处理POI生成embedding向量保存至vector_store，同时保存至poi表
     *
     * @param poisList
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void asyncEmbeddingAndSaveUpdate(List<Pois> poisList) {
        try {
            log.info("Async embedding and save POIs: size {}", poisList.size());
            List<Document> documents = poisList.stream().map(pois -> {
                String textTemplate = "%s. Location:%s. Type:%s. Description:%s";
                String text = String.format(textTemplate, pois.getName(), pois.getCity() + pois.getAddress(), pois.getType(), pois.getDescription());
                Map<String, Object> metadata = new HashMap<>();
                // 添加元数据
                metadata.put("entity", "Pois");
                return Document.builder()
                        .id(pois.getPoiId().toString())
                        .metadata(BeanUtil.beanToMap(pois, metadata,
                                // 只保留必要字段 poiId, externalApiId, name, type, avgCost, city, rating
                                CopyOptions.create().setIgnoreProperties(Constant.POIS_METADATA_IGNORE_FIELDS).ignoreNullValue()))
                        .text(text).build();
            }).toList();
            // 保存或更新vectorStore中的内容,先删除旧数据,再添加新数据
            vectorStore.delete(documents.stream().map(Document::getId).toList());
            vectorStore.add(documents);
            log.info("Async embedding success: Document size {}", documents.size());
            if (saveOrUpdateBatch(poisList)) {
                log.info("Save or update POIs to database success: size {}", poisList.size());
            }
        } catch (Exception e) {
            // 添加日志以更方便查看错误信息
            log.error("Async embedding and save POIs failed.", e);
            // 继续抛出异常让事务回滚
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Pois> semanticSearchPois(String queryText, String city, int topK) {
        log.info("Performing semantic search pois for query: {}, {}, {}", queryText, city, topK);
        if (queryText == null || queryText.trim().isEmpty()) {
            throw new BusinessException(Constant.POIS_SEARCH_INVALID_PARAM);
        }
        // 使用vector store进行相似度搜索, 注意''单引号
        String filterExpression = "entity == 'Pois'";
        if (city != null && !city.trim().isEmpty()){
            city = city.endsWith("市") ? city : city + "市";
            filterExpression += " && city == '%s'".formatted(city);
        }
        log.info("Filter expression: {}", filterExpression);
        SearchRequest searchRequest = SearchRequest.builder()
                .query(queryText)
                .topK(topK)
                .filterExpression(filterExpression).build();
        List<Document> documents = vectorStore.similaritySearch(searchRequest);
        if (documents == null || documents.isEmpty()) {
            log.info("Semantic search pois failed: no results found.");
            return Collections.emptyList();
        }
        // 从数据库中查询POI信息
        List<UUID> poiIds = documents.stream().map(document -> {
            log.info("Document: {}, score: {}", document.getId(), document.getScore());
            return UUID.fromString(document.getId());
        }).toList();
        Map<UUID, Pois> idToPoi = listByIds(poiIds).stream().collect(Collectors.toMap(Pois::getPoiId, pois -> pois));
        // 按照相似度排序
        List<Pois> pois = poiIds.stream().map(idToPoi::get).filter(Objects::nonNull).toList();
        log.info("Semantic search pois success: size {}", pois.size());
        return pois;
    }

}




