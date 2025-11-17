package com.bread.traveler.mapper;

import com.bread.traveler.entity.Pois;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author huang
 * @description 针对表【pois】的数据库操作Mapper
 * @createDate 2025-11-14 12:09:43
 * @Entity com.bread.traveler.entity.Pois
 */
@Mapper
public interface PoisMapper extends BaseMapper<Pois> {

    /**
     * 语义搜索POI，使用pgvector进行向量相似度搜索
     * @param queryEmbedding 查询向量
     * @return 匹配的POI列表
     */
    @Select("""
        SELECT poi_id, external_api_id, name, type, address, latitude, longitude,
               description, description_embedding, opening_hours, avg_visit_duration,
               price_level, created_by_user_id, created_at
        FROM pois
        WHERE description_embedding IS NOT NULL
        ORDER BY description_embedding <#> #{queryEmbedding}
        LIMIT 20
    """)
    List<Pois> semanticSearch(@Param("queryEmbedding") float[] queryEmbedding);

}




