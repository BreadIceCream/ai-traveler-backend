package com.bread.traveler.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class TripMemberPendingRequestDto {

    private UUID tripId;
    private String title;
    private Long pendingRequestCount;

}
