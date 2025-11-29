package com.bread.traveler.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 非POI类型枚举
 * tips：解决映射问题，
 * 1.枚举类型的enum值添加@EnumValue注解
 * 2.创建自定义的EnumTypeHandler，在类上添加@MappedTypes(Enum.class)，@MappedJdbcTypes(JdbcType.OTHER)注解
 * 3.配置文件中指定mybatis-plus.configuration.default-enum-type-handler为自定义的EnumTypeHandler
 * 4.在实体类上要添加autoResultMap=true，且实体类要有TableId
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
