package com.bread.traveler.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Schema(description = "网页提取参数")
public class ExtractWebPageDto {

    @Schema(description = "城市", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String city;

    @Schema(description = "网页ID")
    private List<UUID> webPageIds;

}
