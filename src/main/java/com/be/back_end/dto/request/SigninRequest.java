package com.be.back_end.dto.request;


import lombok.Data;

@Data
public class SigninRequest {
    private String email;
    private String password;
}