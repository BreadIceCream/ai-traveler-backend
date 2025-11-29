package com.bread.traveler.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRole {
    @EnumValue
    OWNER(3),
    @EnumValue
    EDITOR(2),
    @EnumValue
    VIEWER(1);

    private final int level;

    /**
     * 判断当前角色是否具有权限
     * @param lowestRole
     * @return
     */
    public boolean hasPermission(MemberRole lowestRole){
        return this.level >= lowestRole.level;
    }


}
