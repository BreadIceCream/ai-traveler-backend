package com.bread.traveler.dto;

import com.bread.traveler.entity.TripMembers;
import com.bread.traveler.enums.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "旅程成员信息")
public class TripMemberDto extends TripMembers {

    @Schema(description = "用户名", example = "testuser")
    private String userName;
    
    @Schema(description = "偏好文本", example = "喜欢历史文化和美食")
    private String preferencesText;

}
