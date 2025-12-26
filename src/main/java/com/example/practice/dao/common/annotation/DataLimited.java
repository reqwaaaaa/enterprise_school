package com.example.practice.dao.common.annotation;

/**
 * 自定义注解：数据权限限制
 * 用于防止低权限用户通过修改参数操作高权限数据
 * 例如：普通用户只能发布 type="0" 的动态/课程/申报
 */

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Component
public @interface DataLimited {

}
