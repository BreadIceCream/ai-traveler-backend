// 1. Vector类型转换器
package com.bread.traveler.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;
import java.util.Arrays;

/**
 * PostgreSQL Vector 类型转换器
 * 将数据库中的 vector 类型与 Java 的 float[] 类型进行转换
 */
@MappedTypes({float[].class})
@MappedJdbcTypes(JdbcType.OTHER)
public class VectorTypeHandler extends BaseTypeHandler<float[]> {

    /**
     * 设置非空参数
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, float[] parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null || parameter.length == 0) {
            ps.setNull(i, Types.OTHER);
            return;
        }

        // 将 double[] 转换为 vector 字符串格式: [1.0,2.0,3.0]
        String vectorStr = Arrays.toString(parameter);

        // 使用 PGobject 设置 vector 类型
        try {
            Class<?> pgObjectClass = Class.forName("org.postgresql.util.PGobject");
            Object pgObject = pgObjectClass.getDeclaredConstructor().newInstance();

            pgObjectClass.getMethod("setType", String.class).invoke(pgObject, "vector");
            pgObjectClass.getMethod("setValue", String.class).invoke(pgObject, vectorStr);

            ps.setObject(i, pgObject);
        } catch (Exception e) {
            throw new SQLException("Failed to set vector parameter", e);
        }
    }

    /**
     * 获取可为空的结果
     */
    @Override
    public float[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseVector(rs.getObject(columnName));
    }

    /**
     * 获取可为空的结果（通过索引）
     */
    @Override
    public float[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseVector(rs.getObject(columnIndex));
    }

    /**
     * 获取可为空的结果（CallableStatement）
     */
    @Override
    public float[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseVector(cs.getObject(columnIndex));
    }

    /**
     * 解析 vector 类型
     * 支持格式: [1.0,2.0,3.0] 或 "(1.0,2.0,3.0)"
     */
    private float[] parseVector(Object value) throws SQLException {
        if (value == null) {
            return null;
        }

        String vectorStr = value.toString();

        // 移除括号和空格
        vectorStr = vectorStr.replaceAll("[\\[\\]\\(\\)\\s]", "");

        if (vectorStr.isEmpty()) {
            return new float[0];
        }

        // 分割并转换为 float[]
        String[] parts = vectorStr.split(",");
        float[] result = new float[parts.length];

        try {
            for (int i = 0; i < parts.length; i++) {
                result[i] = Float.parseFloat(parts[i].trim());
            }
            return result;
        } catch (NumberFormatException e) {
            throw new SQLException("Failed to parse vector: " + vectorStr, e);
        }
    }
}