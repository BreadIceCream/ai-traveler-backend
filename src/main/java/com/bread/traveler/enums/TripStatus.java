package com.bread.traveler.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum TripStatus {
    @EnumValue
    PLANNING,
    @EnumValue
    IN_PROGRESS,
    @EnumValue
    COMPLETED,
    @EnumValue
    CANCELLED
}
