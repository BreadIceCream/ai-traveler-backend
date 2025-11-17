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

    public static class ShowField{
        //设置后返回 poi 商业信息，包括营业时间、评分等
        public static final String BUSINESS = "business";
        //设置后返回 poi 图片相关信息
        public static final String PHOTOS = "photos";
    }

}
