package com.example.practice.dao.common.handler;

import com.example.practice.dao.common.exception.ConditionException;
import com.example.practice.service.util.JsonResponse;  // 你的统一响应类，稍后定义
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 * 最高优先级，捕获所有 Controller 抛出的异常
 * 特别是 ConditionException，返回 {code, message}
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)  // 最高优先级
public class CommonGlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonResponse<String> commonExceptionHandler(HttpServletRequest request, Exception e) {
        String message = e.getMessage();
        if (e instanceof ConditionException) {
            String code = ((ConditionException) e).getCode();
            return new JsonResponse<>(code, message);
        } else {
            // 未预期的异常，返回 500
            e.printStackTrace();  // 打印栈迹便于调试
            return new JsonResponse<>("500", message != null ? message : "系统错误");
        }
    }
}