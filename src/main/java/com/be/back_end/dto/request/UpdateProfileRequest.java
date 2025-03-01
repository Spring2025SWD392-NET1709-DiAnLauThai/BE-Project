package com.be.back_end.dto.request;

import com.be.back_end.enums.ActivationEnums;
import com.be.back_end.enums.RoleEnums;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateProfileRequest {
    private String id;
    private String email;
    private String name;
    private String address;
    private String phone;
    private LocalDate dateOfBirth;
    private MultipartFile imageFile;
}
