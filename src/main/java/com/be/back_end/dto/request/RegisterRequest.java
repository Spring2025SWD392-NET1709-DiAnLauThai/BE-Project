package com.be.back_end.dto.request;

import com.be.back_end.enums.RoleEnums;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequest {
    @Email
    @NotBlank(message = "email cannot be blank")
    private String email;
    @NotBlank(message = "name cannot be blank")
    private String name;
    @Size(min=5,max=15,message = "password should be in between 5 and 15 character")
    @NotBlank(message = "password cannot be blank")
    private String password;
    @NotBlank(message = "phone cannot be blank")
    private String phone;
    @NotNull(message = "role cannot be null")
    private RoleEnums role;
}
