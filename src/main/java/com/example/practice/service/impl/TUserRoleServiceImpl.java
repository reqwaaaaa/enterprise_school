package com.example.practice.service.impl;

import com.example.practice.entity.TUserRole;
import com.example.practice.mapper.TUserRoleMapper;
import com.example.practice.service.TUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户角色关联表 - RBAC权限模型 服务实现类
 * </p>
 *
 * @author Naiweilanlan
 * @since 2025-12-24
 */
@Service
public class TUserRoleServiceImpl extends ServiceImpl<TUserRoleMapper, TUserRole> implements TUserRoleService {

}
