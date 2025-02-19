package com.be.back_end.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequest {
    private String email;
    private String name;
    private String password;
    private String address;
    private String phone;
    private LocalDate dateOfBirth;

}
