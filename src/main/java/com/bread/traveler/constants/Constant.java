package com.bread.traveler.constants;

import java.util.Set;

public class Constant {

    public static final String USERS_NOT_EXIST = "用户不存在";
    public static final String USERS_CREATE_FAILED = "用户名已存在，创建失败";
    public static final String POIS_NOT_FOUND = "地点未找到";
    public static final String POIS_SEARCH_FAILED = "搜索地点失败";
    public static final String POIS_SEARCH_INVALID_PARAM = "搜索关键词不能为空";
    public static final String POIS_SAVE_FAILED = "保存地点失败";
    public static final String POIS_SEARCH_NO_RESULT = "未找到相关地点";
    public static final String POIS_GENERATE_DESCRIPTION_FAILED = "生成描述信息失败";
    public static final Integer POIS_SEARCH_EXTERNAL_API_RETURN_NUMBER = 3;
    public static final String POIS_SEMANTIC_SEARCH_TOP_K = "5";
    public static final String[] POIS_METADATA_IGNORE_FIELDS = {"address", "latitude", "longitude", "description", "openingHours", "avgVisitDuration", "createdAt", "photos", "phone"};
    public static final String USERS_PREFERENCES_UPDATE_FAILED = "用户偏好更新失败";
    public static final String USERS_FIND_SIMILAR_LIMIT = "5";
    public static final String USERS_FIND_SIMILAR_NO_RESULT = "未找到相似用户";
    public static final String WEB_SEARCH_FAILED = "网页搜索失败";
    public static final Integer WEB_SEARCH_COUNT = 10;
    public static final int CHAT_MEMORY_MAX_MESSAGES = 50;
    public static final String CONVERSATION_NOT_EXIST = "对话不存在";
    public static final String CONVERSATION_PERMISSION_DENIED = "没有权限查看该对话";

    public static class SHOW_FIELD {
        //设置后返回 poi 商业信息，包括营业时间、评分等
        public static final String BUSINESS = "business";
        //设置后返回 poi 图片相关信息
        public static final String PHOTOS = "photos";
    }

    public static class WEB_SEARCH_DOMAIN{
        public static final String BING = "www.bing.com";
        public static final String DIANPING = "www.dianping.com";
        public static final String XIAOHONGSHU = "www.xiaohongshu.com";
    }

}
