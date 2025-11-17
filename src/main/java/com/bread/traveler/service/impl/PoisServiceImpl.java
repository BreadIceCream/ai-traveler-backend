package com.bread.traveler.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bread.traveler.constants.Constant;
import com.bread.traveler.entity.Pois;
import com.bread.traveler.exception.BusinessException;
import com.bread.traveler.service.PoisService;
import com.bread.traveler.mapper.PoisMapper;
import com.bread.traveler.utils.GaoDeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.StructuredOutputConverter;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.zhipuai.ZhiPuAiChatOptions;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.*;

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
    @Qualifier("zhiPuChatClient")
    private ObjectProvider<ChatClient> zhiPuChatClientProvider;
    @Autowired
    @Qualifier("openAiChatClient")
    private ObjectProvider<ChatClient> openAiChatClientProvider;

    @Autowired
    @Lazy
    private PoisService selfProxy;

    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(10);

    @Override
    public Pois getPoiById(UUID poiId) {
        log.info("Getting POI by ID: {}", poiId);
        Pois pois = getById(poiId);
        if (pois == null){
            throw new BusinessException(Constant.POIS_NOT_FOUND);
        }
        return pois;
    }

    @Override
    public List<Pois> searchPoiFromDb(String city, String keywords) {
        log.info("Searching POIs from database: city={}, keywords={}", city, keywords);
        LambdaQueryChainWrapper<Pois> wrapper = lambdaQuery().like(Pois::getName, keywords);
        if (city != null && !city.trim().isEmpty()){
            wrapper.like(Pois::getCity, city);
        }
        List<Pois> list = wrapper.list();
        if (list == null || list.isEmpty()){
            throw new BusinessException(Constant.POIS_SEARCH_FAILED + "，请使用高德搜索尝试");
        }
        return list;
    }

    @Override
    public List<Pois> searchPoiFromExternalApi(String city, String keywords) {
        log.info("Searching POIs from external API: city={}, keywords={}", city, keywords);
        if (keywords == null || keywords.trim().isEmpty()) {
            throw new RuntimeException(Constant.POIS_SEARCH_INVALID_PARAM);
        }
        GaoDeUtils.SearchPoiParam.SearchPoiParamBuilder paramBuilder = GaoDeUtils.SearchPoiParam.builder()
                .keywords(keywords).showFields(List.of(Constant.ShowField.BUSINESS, Constant.ShowField.PHOTOS))
                .pageSize(5);
        if (city != null && !city.trim().isEmpty()) {
            paramBuilder.city(city).cityLimit(true);
        }
        GaoDeUtils.SearchPoiParam param = paramBuilder.build();
        JSONObject results = null;
        try {
            results = gaoDeUtils.searchPoi(param);
        } catch (IOException e) {
            throw new RuntimeException(Constant.POIS_SEARCH_FAILED);
        }
        if (!Objects.equals(results.getStr("status"), "1")){
            // 如果status不为1，搜索失败
            throw new BusinessException(Constant.POIS_SEARCH_FAILED + ", " + results.getStr("info"));
        }
        JSONArray pois = results.getJSONArray("pois");
        // 解析POI数据，生成description描述信息，封装成poi对象
        Future<List<String>> descriptionsTask = THREAD_POOL.submit(() -> {
            List<String> poisName = pois.stream().map(poi -> {
                JSONObject poiJson = (JSONObject) poi;
                return poiJson.getStr("name");
            }).toList();
            return generateDescriptions(poisName);
        });
        List<Pois> parsedPois = pois.stream().map(poi -> {
            JSONObject poiJson = (JSONObject) poi;
            return parsePoi(poiJson);
        }).toList();
        try {
            List<String> descriptions = descriptionsTask.get();
            for (int i = 0; i < parsedPois.size(); i++) {
                Pois poi = parsedPois.get(i);
                poi.setDescription(descriptions.get(i));
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(Constant.POIS_GENERATE_DESCRIPTION_FAILED);
        }
        if (parsedPois.isEmpty()){
            throw new BusinessException(Constant.POIS_SEARCH_NO_RESULT);
        }
        //异步处理embedding向量，写入数据库 todo采用消息队列
        THREAD_POOL.submit(() -> selfProxy.asyncEmbeddingAndSave(parsedPois));
        return parsedPois;
    }

    /**
     * 生成POI描述
     * @param poisName
     * @return
     */
    private List<String> generateDescriptions(List<String> poisName){
        log.info("Generating descriptions for POIs: {}", poisName);
        PromptTemplate promptTemplate = new PromptTemplate(new ClassPathResource("prompts/generateDescriptionTemplate.md"));
        Prompt prompt = promptTemplate.create(Map.of("poisName", poisName));
        List<String> descriptions = zhiPuChatClientProvider.getObject().prompt(prompt)
                .options(ChatOptions.builder().model("GLM-4-FlashX-250414").temperature(0.5).build())
                .call().entity(new ParameterizedTypeReference<>() {});
        log.info("Descriptions generated success!");
        return descriptions;
    }

    /**
     * 解析POI数据，转为POI对象
     * @param poiJson
     * @return
     */
    private Pois parsePoi(JSONObject poiJson) {
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
        if (poiJson.containsKey("business")){
            JSONObject business = poiJson.getJSONObject("business");
            poisBuilder
                    .openingHours(business.getStr("opentime_week", ""))
                    .phone(business.getStr("tel", ""))
                    .rating(business.getStr("rating", ""))
                    .avgCost(business.getStr("cost", ""));
        }
        // 设置图片信息
        if (poiJson.containsKey("photos")){
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
     * @param poisList
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void asyncEmbeddingAndSave(List<Pois> poisList) {
        try {
            log.info("Async embedding and save POIs: size {}", poisList.size());
            List<Document> documents = poisList.stream().map(pois -> {
                String textTemplate = "%s, type:%s, description:%s";
                String text = String.format(textTemplate, pois.getName(), pois.getType(), pois.getDescription());
                return Document.builder()
                        .id(pois.getPoiId().toString())
                        .metadata(BeanUtil.beanToMap(pois, new HashMap<>(), CopyOptions.create().ignoreNullValue()))
                        .text(text).build();
            }).toList();
            vectorStore.add(documents);
            log.info("Async embedding success: Document size {}", documents.size());
            if (saveBatch(poisList)) {
                log.info("Save POIs to database success: size {}", poisList.size());
            }
        } catch (Exception e) {
            // 添加日志以更方便查看错误信息
            log.error("Async embedding and save POIs failed.", e);
            // 继续抛出异常让事务回滚
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Pois> semanticSearchPois(String queryText) {
//        log.info("Performing semantic search for query: {}", queryText);
//        if (queryText == null || queryText.trim().isEmpty()) {
//            throw new BusinessException(Constant.POIS_SEARCH_INVALID_PARAM);
//        }
//        try {
//            // 生成查询文本的向量嵌入
//            float[] queryEmbedding = embeddingModel.embed(queryText);
//
//            // 使用pgvector进行向量相似度搜索
//            // 这里使用原生SQL查询，因为MyBatis-Plus对向量搜索的支持有限 todo
//            vectorStore.add();
//
//            log.info("Found {} POIs matching the query", pois.size());
//            return pois;
//
//        } catch (Exception e) {
//            log.error("Failed to perform semantic search for query: {}", queryText, e);
//            // 如果向量搜索失败，返回空列表或fallback到文本搜索
//            return fallbackTextSearch(queryText);
//        }
        return null;
    }

    /**
     * 回退的文本搜索方法
     */
    private List<Pois> fallbackTextSearch(String queryText) {
        log.warn("Falling back to text-based search for query: {}", queryText);
        LambdaQueryWrapper<Pois> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Pois::getName, queryText)
               .or()
               .like(Pois::getDescription, queryText)
               .orderByDesc(Pois::getCreatedAt);

        List<Pois> pois = list(wrapper);
        log.info("Found {} POIs using text search", pois.size());
        return pois;
    }

}




