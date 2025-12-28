package com.example.practice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.practice.entity.TUser;
import com.example.practice.entity.TUserInfo;

/**
 * <p>
 * 用户表 - 统一用户管理系统 服务类
 * </p>
 *
 * @author Naiweilanlan
 * @since 2025-12-24
 */
public interface TUserService extends IService<TUser> {

    // 注册（加密密码 + 盐）
    void register(String username, String password, String email, String phone, String userType, Long organizationId);

    // 登录（验证密码 + 生成 JWT）
    String login(String username, String password);

    // 获取用户信息（包含扩展 info）
    TUser getUserWithInfo(Long userId);

    // 更新用户（权限检查）
    void updateUser(TUser user);

    // 树形组织用户查询
    List<TUser> getUsersByOrganization(Long organizationId);
}
