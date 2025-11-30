package com.bread.traveler.dto;

import com.bread.traveler.enums.NonPoiType;import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(description = "非POI项数据传输对象")
public class NonPoiItemDto {

    @Schema(description = "非POI项ID")
    private UUID id;
    
    @Schema(description = "非POI项类型", example = "ACTIVITY")
    private NonPoiType type;
    
    @Schema(description = "标题", example = "全聚德烤鸭店")
    private String title;
    
    @Schema(description = "描述", example = "正宗北京烤鸭")
    private String description;
    
    @Schema(description = "城市", example = "北京")
    private String city;
    
    @Schema(description = "活动时间", example = "10:00-22:00")
    private String activityTime;
    
    @Schema(description = "预计地址", example = "北京市东城区前门大街32号")
    private String estimatedAddress;
    
    @Schema(description = "额外信息", example = "人均消费200-300元")
    private String extraInfo;
    
    @Schema(description = "来源URL", example = "https://example.com")
    private String sourceUrl;
}
