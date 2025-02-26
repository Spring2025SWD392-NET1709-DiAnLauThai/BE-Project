package com.be.back_end.dto.response;

import com.be.back_end.enums.ActivationEnums;
import com.be.back_end.enums.RoleEnums;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AccountCreationResponse {
    private UUID id;
    private String email;
    private String name;
    private String phone;
    private String address;
    private RoleEnums role;
    private ActivationEnums status;
    private LocalDate dateOfBirth;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}