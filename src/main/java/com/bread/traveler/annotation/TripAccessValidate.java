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
public @interface TripAccessValidate {

    // 对Trip修改的默认访问权限为OWNER
    MemberRole lowestRole() default MemberRole.OWNER;

}
