package com.be.back_end.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class AccountDTO {

    private String email;
    private String name;
    private String address;
    private String phone;

    private LocalDate dateOfBirth;
    private boolean isDeleted;

}
