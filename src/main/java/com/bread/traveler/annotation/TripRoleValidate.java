package com.bread.traveler.annotation;

import com.bread.traveler.enums.MemberRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author huang
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TripRoleValidate {

    // 这里设置默认设置为EDITOR。默认设置表示修改的最低权限
    MemberRole lowestRole() default MemberRole.OWNER;

}
