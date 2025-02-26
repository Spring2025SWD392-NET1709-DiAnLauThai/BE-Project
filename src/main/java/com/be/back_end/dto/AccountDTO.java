package com.be.back_end.dto;

import com.be.back_end.enums.ActivationEnums;
import com.be.back_end.enums.RoleEnums;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class AccountDTO {
    private String id;
    private String email;
    private String name;
    private String address;
    private String phone;
    private LocalDate dateOfBirth;
    private ActivationEnums status;

    private RoleEnums role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

