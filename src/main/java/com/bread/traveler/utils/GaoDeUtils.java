package com.bread.traveler.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Builder;
import lombok.Data;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GaoDeUtils {

    public static final String SEARCH_POI_URL = "https://restapi.amap.com/v5/place/text";

    @Value("${gaode.api-key}")
    private String apiKey;

    @Builder
    @Data
    public static class SearchPoiParam {
        private String keywords;
        private String city;
        private Boolean cityLimit;
        private List<String> showFields;
        private Integer pageSize;
        private Integer pageNum;
    }

    /**
     * 搜索POI
     * @param param 搜索参数
     * @return 搜索结果JSON字符串
     * @throws IOException
     */
    public JSONObject searchPoi(SearchPoiParam param) throws IOException {
        // 将param对象转为Map
        Map<String, Object> map = new HashMap<>();
        BeanUtil.beanToMap(param, map, true, true);
        map.put("key", apiKey);
        if (param.showFields != null && !param.showFields.isEmpty()){
            map.put("show_fields", String.join(",", param.showFields));
        }
        // 转换map的value类型为String
        Map<String, String> paramMap = map.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), String.valueOf(entry.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        String url = UrlParamsUtils.buildUrlWithParams(SEARCH_POI_URL, paramMap);
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                String json = EntityUtils.toString(entity, "UTF-8");
                return json == null ? null : JSONUtil.parseObj(json);
            }
        }
    }

}
