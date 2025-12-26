package com.example.practice.api.support;

import com.example.practice.dao.common.exception.ConditionException;
import com.example.practice.service.util.TokenUtil;  // TokenUtil 放在 service.util 下
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 用户上下文支持类
 * 作用：从当前请求的 Authorization Header 中提取 Bearer Token，
 *      调用 TokenUtil 验证并获取当前登录用户的 userId。
 * 使用场景：所有需要登录的 Controller 方法中注入此 bean，调用 getCurrentUserId()
 */
@Component
public class UserSupport {

  /**
   * 获取当前登录用户的 ID
   * @return 当前用户 ID
   * @throws ConditionException 如果未登录、token 格式错误或验证失败
   */
  public Long getCurrentUserId() {
    ServletRequestAttributes attributes =
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

    if (attributes == null) {
      throw new ConditionException("非法请求，无法获取上下文");
    }

    String authHeader = attributes.getRequest().getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new ConditionException("未登录，请提供有效的 Authorization Bearer Token");
    }

    String token = authHeader.substring(7);  // 去除 "Bearer " 前缀

    Long userId = TokenUtil.verifyToken(token);
    if (userId == null || userId <= 0) {
      throw new ConditionException("非法 token，用户认证失败");
    }

    return userId;
  }
}