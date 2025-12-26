package com.example.practice.dao.common.constant;

/**
 * 用户相关常量
 * 与 t_user 和 t_user_info 表对应
 */
public interface UserConstant {

  String GENDER_MALE = "male";     // 性别：男

  String GENDER_FEMALE = "female"; // 性别：女

  String GENDER_OTHER = "other";   // 性别：其他

  String DEFAULT_BIRTH = "2000-01-01";  // 默认生日

  String DEFAULT_NICK = "新用户";       // 默认昵称

  String DEFAULT_AVATAR = "https://github.com/reqwaaaaa/reqwaaaaa.github.io/blob/main/assets/images/avatar.png";

  String USER_STATUS_ACTIVE = "active";     // 用户状态：活跃

  String USER_STATUS_INACTIVE = "inactive"; // 未激活

  String USER_STATUS_BANNED = "banned";     // 封禁
}