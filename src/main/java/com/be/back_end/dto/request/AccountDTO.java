package com.be.back_end.dto.request;

import com.be.back_end.enums.ActivationEnums;
import com.be.back_end.enums.RoleEnums;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private String image_url;
    private RoleEnums role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

