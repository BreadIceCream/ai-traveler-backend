package com.bread.traveler.annotation;

import com.bread.traveler.enums.MemberRole;

import java.lang.annotation.*;

/**
 * @author huang
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TripAccessValidate {

    // 对Trip修改的默认访问权限为OWNER
    MemberRole lowestRole() default MemberRole.EDITOR;

}
