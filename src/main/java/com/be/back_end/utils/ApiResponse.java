package com.be.back_end.utils;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private int code;
    private T data;
    private String message;

    public ApiResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }
}
