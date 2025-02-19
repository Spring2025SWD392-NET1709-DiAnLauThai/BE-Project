package com.be.back_end.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenValidateDTO {
    private boolean valid;

}