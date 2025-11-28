package com.bread.traveler.constants;

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
    public static final String PREV_ITEM_NOT_FOUND = "无法获取起点，无法规划路径";
    public static final String NON_POI_ITEM_NOT_FOUND = "未找到该对象";
    public static final String TRIP_DAY_EXISTS = "该日程已经存在";
    public static final String TRIP_DAY_CREATE_FAILED = "日程创建失败";
    public static final String TRIP_DAY_NOT_EXIST = "该日程不存在";
    public static final String TRIP_CREATE_FAILED = "行程创建失败";
    public static final String TRIP_NOT_EXIST = "该行程不存在";
    public static final String TRIP_UPDATE_FAILED = "行程信息更新失败";
    public static final String TRIP_DELETE_FAILED = "行程删除失败";
    public static final String TRIP_DAY_DELETE_FAILED = "日程删除失败";
    public static final String WISHLIST_ITEM_EXISTS = "心愿单中该事项已存在，不要重复添加";
    public static final String WISHLIST_ITEM_NOT_EXISTS = "该事项不存在";
    public static final String TRIP_DAY_NO_ITEMS = "当前日程为空";
    public static final String TRIP_DAY_RE_PLAN_FAILED = "AI规划日程失败";
    public static final String TRIP_DAY_DATE_NOT_IN_TRIP_RANGE = "日期超出行程时间";
    public static final String WISHLIST_EMPTY = "心愿单为空";
    public static final String TRIP_DAY_ITEM_EXISTS = "日程中已存在，不要重复添加";
    public static final String TRIP_AI_GENERATE_FAILED = "AI规划旅程失败";
    public static String DESTINATION_ITEM_NOT_FOUND = "未找到目的地";

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
