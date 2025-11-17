package com.bread.traveler.utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class UrlParamsUtils {

    /**
     * 将Map集合中的key-value拼接到URL中作为参数
     * @param baseUrl 基础URL
     * @param params  参数Map集合
     * @return 拼接后的完整URL
     */
    public static String buildUrlWithParams(String baseUrl, Map<String, String> params) {
        if (baseUrl == null || baseUrl.isEmpty()) {
            throw new RuntimeException("baseUrl不能为空");
        }
        if (params == null || params.isEmpty()) {
            return baseUrl;
        }
        // 判断URL是否已经包含参数
        String separator = baseUrl.contains("?") ? "&" : "?";
        // 拼接参数
        String paramString = params.entrySet().stream()
                .filter(entry -> entry.getKey() != null && entry.getValue() != null)
                .map(entry -> encodeParam(entry.getKey()) + "=" + encodeParam(entry.getValue()))
                .collect(Collectors.joining("&"));
        return paramString.isEmpty() ? baseUrl : baseUrl + separator + paramString;
    }


    /**
     * URL编码参数值，避免特殊字符问题
     *
     * @param value 参数值
     * @return 编码后的值
     */
    private static String encodeParam(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

}
