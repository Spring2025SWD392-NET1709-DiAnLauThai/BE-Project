package com.be.back_end.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class AccountDTO {
    private UUID id;
    private String email;
    private String name;
    private String address;
    private String phone;
    private String password;
    private LocalDate dateOfBirth;
    private String role;
    private String status;
    private boolean isDeleted;

}

