package com.bread.traveler.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户创建信息")
public class UserDto {

    @Schema(description = "用户名", example = "testuser")
    private String username;
    @Schema(description = "密码", example = "password")
    private String password;
    @Schema(description = "偏好文本", example = "喜欢历史文化和美食", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String preferencesText;

}
