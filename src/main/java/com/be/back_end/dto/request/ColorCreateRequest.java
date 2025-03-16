package com.be.back_end.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ColorCreateRequest {

    private String colorName;
    private String colorCode;
}
