package com.be.back_end.utils;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private String code;
    private T data;
    private String message;

    public ApiResponse(String code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }
}
