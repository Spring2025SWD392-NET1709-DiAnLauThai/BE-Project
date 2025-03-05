package com.be.back_end.dto.request;

import com.be.back_end.enums.ActivationEnums;
import com.be.back_end.enums.RoleEnums;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Date of Birth in UTC format (yyyy-MM-dd)")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private LocalDate dateOfBirth;
    private MultipartFile imageFile;
}
