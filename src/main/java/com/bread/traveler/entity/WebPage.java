package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

import lombok.Data;

/**
 * 
 * @TableName web_page
 */
@TableName(value ="web_page")
@Data
@Schema(description = "网页信息")
public class WebPage implements Serializable {
    /**
     * 
     */
    @TableId(value = "id")
    @Schema(description = "网页ID")
    private UUID id;

    /**
     * 
     */
    @TableField(value = "conversation_id")
    @Schema(description = "会话ID")
    private UUID conversationId;

    /**
     * 网页标题
     */
    @TableField(value = "name")
    @Schema(description = "网页名称", example = "故宫博物院官网")
    private String name;

    /**
     * 
     */
    @TableField(value = "url")
    @Schema(description = "网页URL", example = "https://www.dpm.org.cn")
    private String url;

    /**
     * 
     */
    @TableField(value = "display_url")
    @Schema(description = "显示URL", example = "www.dpm.org.cn")
    private String displayUrl;

    /**
     * 
     */
    @TableField(value = "snippet")
    @Schema(description = "网页摘要", example = "故宫博物院官方网站...")
    private String snippet;

    /**
     * 
     */
    @TableField(value = "summary")
    @Schema(description = "网页总结", example = "详细介绍故宫的历史和文化...")
    private String summary;

    /**
     * 
     */
    @TableField(value = "site_name")
    @Schema(description = "网站名称", example = "百度")
    private String siteName;

    /**
     * 
     */
    @TableField(value = "date_published")
    @Schema(description = "发布日期")
    private OffsetDateTime datePublished;

    /**
     * 
     */
    @TableField(value = "created_at")
    @Schema(description = "创建时间")
    private OffsetDateTime createdAt;
}