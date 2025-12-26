package com.example.practice.dao.common.constant;

/**
 * 角色常量
 * 与 t_role 表的 role_code 字段对应
 */
public interface AuthRoleConstant {

    String ROLE_ADMIN = "ROLE_ADMIN";           // 系统管理员（最高权限）

    String ROLE_SCHOOL_ADMIN = "ROLE_SCHOOL_ADMIN"; // 高校管理员

    String ROLE_ENTERPRISE_ADMIN = "ROLE_ENTERPRISE_ADMIN"; // 企业管理员

    String ROLE_TEACHER = "ROLE_TEACHER";       // 教师（可发布课程、考评）

    String ROLE_STUDENT = "ROLE_STUDENT";       // 学生（学习、投递简历）

    String ROLE_HR = "ROLE_HR";                 // 企业HR（发布招聘、管理投递）

    String ROLE_LV0 = "ROLE_LV0";               // 普通用户（最低权限，受限最多）
}