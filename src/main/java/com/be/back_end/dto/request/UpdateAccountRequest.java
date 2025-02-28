package com.be.back_end.dto.request;

import com.be.back_end.enums.ActivationEnums;
import com.be.back_end.enums.RoleEnums;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateAccountRequest {
    private String id;
    private ActivationEnums status;
    private RoleEnums role;
}
