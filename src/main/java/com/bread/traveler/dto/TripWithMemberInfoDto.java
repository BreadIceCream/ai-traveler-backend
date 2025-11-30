package com.bread.traveler.dto;

import com.bread.traveler.enums.MemberRole;
import com.bread.traveler.enums.TripStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Schema(description = "带成员信息的旅程")
public class TripWithMemberInfoDto {

    @Schema(description = "旅程ID")
    private UUID tripId;
    @Schema(description = "所有者ID")
    private UUID ownerId;
    @Schema(description = "标题", example = "北京三日游")
    private String title;
    @Schema(description = "目的地城市", example = "北京")
    private String destinationCity;
    @Schema(description = "开始日期", example = "2024-12-01")
    private LocalDate startDate;
    @Schema(description = "结束日期", example = "2024-12-03")
    private LocalDate endDate;
    @Schema(description = "总预算", example = "3000.00")
    private BigDecimal totalBudget;
    @Schema(description = "状态", example = "IN_PROGRESS")
    private TripStatus status;
    @Schema(description = "描述", example = "北京三日游计划")
    private String description;
    @Schema(description = "创建时间")
    private OffsetDateTime createdAt;
    @Schema(description = "是否私有", example = "false")
    private Boolean isPrivate;
    @Schema(description = "成员角色", example = "OWNER")
    private MemberRole memberRole;
    @Schema(description = "是否通过审批", example = "true")
    private Boolean isPass;
    @Schema(description = "加入时间")
    private OffsetDateTime joinedAt;

}
