package com.example.practice.service.impl;

import com.example.practice.entity.TUser;
import com.example.practice.mapper.TUserMapper;
import com.example.practice.service.TUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 - 统一用户管理系统 服务实现类
 * </p>
 *
 * @author Naiweilanlan
 * @since 2025-12-24
 */
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements TUserService {

}
