package com.bread.traveler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
public class WebPage implements Serializable {
    /**
     * 
     */
    @TableId(value = "id")
    private UUID id;

    /**
     * 
     */
    @TableField(value = "conversation_id")
    private UUID conversationId;

    /**
     * 网页标题
     */
    @TableField(value = "name")
    private String name;

    /**
     * 
     */
    @TableField(value = "url")
    private String url;

    /**
     * 
     */
    @TableField(value = "display_url")
    private String displayUrl;

    /**
     * 
     */
    @TableField(value = "snippet")
    private String snippet;

    /**
     * 
     */
    @TableField(value = "summary")
    private String summary;

    /**
     * 
     */
    @TableField(value = "site_name")
    private String siteName;

    /**
     * 
     */
    @TableField(value = "date_published")
    private OffsetDateTime datePublished;

    /**
     * 
     */
    @TableField(value = "created_at")
    private OffsetDateTime createdAt;
}