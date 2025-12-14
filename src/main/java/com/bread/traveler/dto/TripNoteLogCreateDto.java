package com.bread.traveler.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "创建日志参数")
public class TripNoteLogCreateDto {

    @Schema(description = "日志文本内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;
    @Schema(description = "是否公开", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Boolean isPublic;

}
