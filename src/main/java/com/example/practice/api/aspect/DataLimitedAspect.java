package com.example.practice.api.aspect;

import com.example.practice.api.support.UserSupport;
import com.example.practice.dao.common.annotation.DataLimited;
import com.example.practice.dao.common.constant.AuthRoleConstant;
import com.example.practice.entity.TUserRole;
import com.example.practice.service.TUserRoleService;
import com.example.practice.dao.common.exception.ConditionException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Order(1)
@Component
@Aspect
public class DataLimitedAspect {

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private TUserRoleService tUserRoleService;

    @Pointcut("@annotation(com.example.practice.dao.common.annotation.DataLimited)")
    public void check() {}

    @Before("check()")
    public void doBefore(JoinPoint joinPoint) {
        Long userId = userSupport.getCurrentUserId();

        List<TUserRole> userRoleList = tUserRoleService.listByUserId(userId);
        Set<String> roleCodeSet = userRoleList.stream()
                .map(TUserRole::getRoleCode)
                .collect(Collectors.toSet());

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            // 示例1：限制课程发布类型（普通用户只能发布 type="0" 的课程）
            if (arg instanceof com.example.practice.entity.TCourse) {
                com.example.practice.entity.TCourse course = (com.example.practice.entity.TCourse) arg;
                if (roleCodeSet.contains(AuthRoleConstant.ROLE_LV0) && !"0".equals(course.getType())) {
                    throw new ConditionException("普通用户只能发布基础课程！");
                }
            }

            // 示例2：限制申报类型
            if (arg instanceof com.example.practice.entity.TApplication) {
                com.example.practice.entity.TApplication app = (com.example.practice.entity.TApplication) arg;
                if (roleCodeSet.contains(AuthRoleConstant.ROLE_LV0) && !"training_subsidy".equals(app.getType())) {
                    throw new ConditionException("普通用户只能申报培训补贴！");
                }
            }

            // 你可以继续添加其他实体检查
        }
    }
}