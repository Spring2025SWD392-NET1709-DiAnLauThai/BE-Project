package com.be.back_end.dto.response;

import com.be.back_end.enums.AccountEnums;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtResponse {
    private String token;
    private String id;
    private String email;
    private String role;
    private String name;
    private String address;
    private String phone;
    private LocalDate dateOfBirth;
    private AccountEnums status;

    // Constructor đầy đủ
    public JwtResponse(String id, String email, String role, 
                      String name, String address, String phone, 
                      LocalDate dateOfBirth, AccountEnums status) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.status = status;
    }

    // Constructor cho signin
    public JwtResponse(String token) {
        this.token = token;
    }

    // Constructor với thông tin cơ bản
    public JwtResponse(String token, String id, String email, String role) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.role = role;
    }
}