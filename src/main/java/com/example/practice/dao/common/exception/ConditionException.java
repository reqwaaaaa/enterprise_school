package com.example.practice.dao.common.exception;

/**
 * 业务异常类
 * 用于抛出可预期的业务错误（如登录失败、权限不足、参数错误）
 * 携带错误码 code，便于前端统一处理
 */
public class ConditionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String code;

    /**
     * 带错误码和消息的构造器
     */
    public ConditionException(String code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 只带消息的构造器，默认 code 为 "500"
     */
    public ConditionException(String message) {
        super(message);
        this.code = "500";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}