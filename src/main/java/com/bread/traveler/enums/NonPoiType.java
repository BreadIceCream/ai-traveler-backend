package com.bread.traveler.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 非POI类型枚举
 * tips：解决映射问题，
 * 1.enum值添加@EnumValue注解
 * 2.创建自定义的EnumTypeHandler，在类上添加@MappedTypes({NonPoiType.class})，@MappedJdbcTypes(JdbcType.OTHER)注解
 * 3.配置文件中指定mybatis-plus.configuration.default-enum-type-handler为自定义的EnumTypeHandler，且在该实体类上要添加autoResultMap=true
 *
 */
public enum NonPoiType {
    @EnumValue
    ACTIVITY,
    @EnumValue
    FOOD,
    @EnumValue
    CULTURE,
    @EnumValue
    OTHER
}
