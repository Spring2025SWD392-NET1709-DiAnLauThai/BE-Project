package com.be.back_end.dto.request;

import com.be.back_end.enums.RoleEnums;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CreateAccountRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Phone is required")
    private String phone;

    private String address;

    @NotNull(message = "Role is required")
    private RoleEnums role;

    private LocalDate dateOfBirth;
}