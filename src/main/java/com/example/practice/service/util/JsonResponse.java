package com.example.practice.service.util;

public class JsonResponse<T> {
    private String code;
    private String message;
    private T data;

    public JsonResponse(T data) {
        this.code = "0";
        this.message = "成功";
        this.data = data;
    }

    public JsonResponse(String code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }

    // getter/setter/toString...
}
