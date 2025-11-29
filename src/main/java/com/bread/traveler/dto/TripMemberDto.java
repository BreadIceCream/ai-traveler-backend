package com.bread.traveler.dto;

import com.bread.traveler.entity.TripMembers;
import com.bread.traveler.enums.MemberRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class TripMemberDto extends TripMembers {

    private String userName;
    private String preferencesText;

}
