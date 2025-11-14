// 1. JSONB 类型转换器
package com.bread.traveler.typehandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;
import java.util.Map;

/**
 * PostgreSQL JSONB 类型转换器
 * 将数据库中的 jsonb 类型与 Java 的 Map<String, Object> 类型进行转换
 */
@MappedTypes({Map.class})
@MappedJdbcTypes(JdbcType.OTHER)
public class JsonbTypeHandler extends BaseTypeHandler<Map<String, Object>> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 设置非空参数
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Map<String, Object> parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null || parameter.isEmpty()) {
            ps.setNull(i, Types.OTHER);
            return;
        }

        try {
            // 将 Map 转换为 JSON 字符串
            String json = objectMapper.writeValueAsString(parameter);

            // 使用 PGobject 设置 jsonb 类型
            Class<?> pgObjectClass = Class.forName("org.postgresql.util.PGobject");
            Object pgObject = pgObjectClass.getDeclaredConstructor().newInstance();

            pgObjectClass.getMethod("setType", String.class).invoke(pgObject, "jsonb");
            pgObjectClass.getMethod("setValue", String.class).invoke(pgObject, json);

            ps.setObject(i, pgObject);
        } catch (JsonProcessingException e) {
            throw new SQLException("Failed to convert Map to JSON", e);
        } catch (Exception e) {
            throw new SQLException("Failed to set jsonb parameter", e);
        }
    }

    /**
     * 获取可为空的结果
     */
    @Override
    public Map<String, Object> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseJsonb(rs.getString(columnName));
    }

    /**
     * 获取可为空的结果（通过索引）
     */
    @Override
    public Map<String, Object> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseJsonb(rs.getString(columnIndex));
    }

    /**
     * 获取可为空的结果（CallableStatement）
     */
    @Override
    public Map<String, Object> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseJsonb(cs.getString(columnIndex));
    }

    /**
     * 解析 JSONB 类型
     */
    private Map<String, Object> parseJsonb(String json) throws SQLException {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }

        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            throw new SQLException("Failed to parse JSONB: " + json, e);
        }
    }
}