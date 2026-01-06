package com.example.practice.controller;

import com.example.practice.entity.TUser;
import com.example.practice.entity.TUserInfo;
import com.example.practice.service.TUserService;
import com.example.practice.service.util.JsonResponse;
import com.example.practice.service.util.RSAUtil;
import com.example.practice.api.support.UserSupport;  // 从 api/support 引入
import com.example.practice.dao.common.annotation.ApiLimitedRole;
import com.example.practice.dao.common.annotation.DataLimited;
import com.example.practice.dao.common.constant.AuthRoleConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "用户管理")
@RestController
@RequestMapping("/user")
public class TUserController {

    @Autowired
    private TUserService tUserService;

    @Autowired
    private UserSupport userSupport;

    @GetMapping("/info")
    @Operation(summary = "获取当前用户信息")
    public JsonResponse<TUser> getUserInfo() {
        Long userId = userSupport.getCurrentUserId();
        TUser user = tUserService.getUserWithInfo(userId);
        return new JsonResponse<>(user);
    }

    @GetMapping("/rsa-public-key")
    @Operation(summary = "获取 RSA 公钥")
    public JsonResponse<String> getRsaPublicKey() {
        return new JsonResponse<>(RSAUtil.getPublicKeyStr());
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public JsonResponse<String> register(@RequestBody TUser user) {
        tUserService.register(user.getUsername(), user.getPassword(), user.getEmail(), user.getPhone(), user.getUserType(), user.getOrganizationId());
        return JsonResponse.success();
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public JsonResponse<String> login(@RequestParam String username, @RequestParam String password) {
        String token = tUserService.login(username, password);
        return new JsonResponse<>(token);
    }

    @PutMapping("/update")
    @Operation(summary = "更新用户信息")
    public JsonResponse<String> updateUser(@RequestBody TUser user) {
        Long userId = userSupport.getCurrentUserId();
        user.setId(userId);  // 防止越权
        tUserService.updateUser(user);
        return JsonResponse.success();
    }

    @ApiLimitedRole(limitedRoleCodeList = {AuthRoleConstant.ROLE_ADMIN})  // 权限注解
    @DataLimited
    @GetMapping("/all")
    @Operation(summary = "获取所有用户 - 管理员")
    public JsonResponse<List<TUser>> getAllUsers() {
        List<TUser> users = tUserService.list();
        return new JsonResponse<>(users);
    }

    // 类似添加 ban/unban 等管理员接口
}