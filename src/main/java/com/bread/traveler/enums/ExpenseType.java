package com.bread.traveler.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ExpenseType {

    @EnumValue
    TRANSPORTATION("交通"),
    @EnumValue
    ACCOMMODATION("住宿"),
    @EnumValue
    DINING("餐饮"),
    @EnumValue
    SIGHTSEEING("游玩"),
    @EnumValue
    SHOPPING("购物"),
    @EnumValue
    OTHER("其他");

    private final String zhName;

}
