package com.bread.traveler.dto;

import com.bread.traveler.enums.MemberRole;
import com.bread.traveler.enums.TripStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class TripWithMemberInfoDto {

    // trip info
    private UUID tripId;
    private UUID ownerId;
    private String title;
    private String destinationCity;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalBudget;
    private TripStatus status;
    private String description;
    private OffsetDateTime createdAt;
    private Boolean isPrivate;
    // member info
    private MemberRole memberRole;
    private Boolean isPass;
    private OffsetDateTime joinedAt;


}
