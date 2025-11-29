package com.bread.traveler.enums;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;

/**
 * 专门处理 PostgreSQL 枚举类型的 Handler
 * 这个类不要放在typehandler包下，否则mybatis-plus会自动扫描注册，导致type属性为Enum.class
 * 现在mybatis-plus会在运行时根据@TableField动态创建
 */
@MappedTypes(Enum.class)
@MappedJdbcTypes(JdbcType.OTHER)
public class EnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

    private final Class<E> type;

    public EnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        // 关键点：使用 Types.OTHER，这样驱动会将其作为枚举发送，而不是 VARCHAR
        ps.setObject(i, parameter.name(), Types.OTHER);
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String s = rs.getString(columnName);
        return s == null ? null : Enum.valueOf(type, s.toUpperCase());
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String s = rs.getString(columnIndex);
        return s == null ? null : Enum.valueOf(type, s.toUpperCase());
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String s = cs.getString(columnIndex);
        return s == null ? null : Enum.valueOf(type, s.toUpperCase());
    }
}