package com.example.practice.api.aspect;

import com.example.practice.api.support.UserSupport;
import com.example.practice.dao.common.annotation.ApiLimitedRole;
import com.example.practice.entity.TUserRole;  // 使用你 Generator 生成的实体
import com.example.practice.service.TUserRoleService;  // Generator 生成的服务
import com.example.practice.dao.common.exception.ConditionException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Order(1)  // 优先级高，先执行
@Component
@Aspect
public class ApiLimitedRoleAspect {

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private TUserRoleService tUserRoleService;

    // 定义切点：所有带有 @ApiLimitedRole 注解的方法
    @Pointcut("@annotation(com.example.practice.dao.common.annotation.ApiLimitedRole)")
    public void check() {}

    @Before("check() && @annotation(apiLimitedRole)")
    public void doBefore(JoinPoint joinPoint, ApiLimitedRole apiLimitedRole) {
        Long userId = userSupport.getCurrentUserId();

        // 查询当前用户的所有角色
        List<TUserRole> userRoleList = tUserRoleService.listByUserId(userId);  // 建议在服务中加此方法

        // 获取注解中禁止的角色代码
        String[] limitedRoleCodeList = apiLimitedRole.limitedRoleCodeList();
        Set<String> limitedSet = Arrays.stream(limitedRoleCodeList).collect(Collectors.toSet());

        // 获取用户实际拥有的角色代码
        Set<String> userRoleCodeSet = userRoleList.stream()
                .map(TUserRole::getRoleCode)  // 确保你的 t_user_role 表有 role_code 字段
                .collect(Collectors.toSet());

        // 如果用户拥有禁止访问的任意角色，则拦截
        userRoleCodeSet.retainAll(limitedSet);
        if (!userRoleCodeSet.isEmpty()) {
            throw new ConditionException("权限不足！");
        }
    }
}