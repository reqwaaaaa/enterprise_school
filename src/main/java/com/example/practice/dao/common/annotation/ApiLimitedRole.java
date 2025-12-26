package com.example.practice.dao.common.annotation;

/**
 * 自定义注解：限制接口访问角色
 * 使用方式：在 Controller 方法上添加
 * @ApiLimitedRole(limitedRoleCodeList = {"ROLE_ADMIN", "ROLE_TEACHER"})
 * 表示只有这些角色的用户才能访问
 */

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Component
public @interface ApiLimitedRole {
    // 如果当前用户拥有其中任意一个角色，则抛出“权限不足”

    String[] limitedRoleCodeList() default {};

}
