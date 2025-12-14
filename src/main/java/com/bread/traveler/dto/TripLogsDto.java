package com.bread.traveler.dto;

import com.bread.traveler.entity.TripLogs;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "旅程日志DTO")
public class TripLogsDto extends TripLogs {

    @Schema(description = "用户名")
    private String username;

}
