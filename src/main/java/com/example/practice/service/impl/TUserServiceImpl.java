package com.example.practice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.practice.dao.common.exception.ConditionException;
import com.example.practice.entity.TUser;
import com.example.practice.entity.TUserInfo;
import com.example.practice.mapper.TUserInfoMapper;  // 假设生成
import com.example.practice.mapper.TUserMapper;
import com.example.practice.service.TUserService;
import com.example.practice.service.util.MD5Util;
import com.example.practice.service.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements TUserService {

    @Autowired
    private TUserInfoMapper tUserInfoMapper;

    @Transactional
    @Override
    public void register(String username, String password, String email, String phone, String userType, Long organizationId) {
        // 检查唯一性
        if (baseMapper.selectOne(new LambdaQueryWrapper<TUser>().eq(TUser::getUsername, username)) != null) {
            throw new ConditionException("用户名已存在");
        }
        // 类似检查 email/phone

        // 生成盐 + 哈希密码
        String salt = UUID.randomUUID().toString().substring(0, 8);
        String passwordHash = MD5Util.sign(password, salt, "UTF-8");

        TUser user = new TUser();
        user.setUsername(username)
                .setEmail(email)
                .setPhone(phone)
                .setPasswordHash(passwordHash)
                .setSalt(salt)
                .setUserType(userType)
                .setOrganizationId(organizationId)
                .setStatus("active");

        baseMapper.insert(user);

        // 添加扩展信息（默认空）
        TUserInfo info = new TUserInfo().setUserId(user.getId());
        tUserInfoMapper.insert(info);
    }

    @Override
    public String login(String username, String password) {
        TUser user = baseMapper.selectOne(new LambdaQueryWrapper<TUser>().eq(TUser::getUsername, username));
        if (user == null || !MD5Util.verify(password, user.getPasswordHash(), user.getSalt(), "UTF-8")) {
            throw new ConditionException("用户名或密码错误");
        }
        try {
            return TokenUtil.generateToken(user.getId());
        } catch (Exception e) {
            throw new ConditionException("Token 生成失败");
        }
    }

    @Override
    public TUser getUserWithInfo(Long userId) {
        TUser user = getById(userId);
        if (user != null) {
            TUserInfo info = tUserInfoMapper.selectOne(new LambdaQueryWrapper<TUserInfo>().eq(TUserInfo::getUserId, userId));
            // 可将 info 合并到 user 或返回 DTO
        }
        return user;
    }

    @Override
    public void updateUser(TUser user) {
        // 权限检查（AOP 或手动）
        updateById(user);
    }

    @Override
    public List<TUser> getUsersByOrganization(Long organizationId) {
        return list(new LambdaQueryWrapper<TUser>().eq(TUser::getOrganizationId, organizationId));
    }
}