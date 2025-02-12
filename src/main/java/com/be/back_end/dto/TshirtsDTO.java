package com.be.back_end.dto;

import com.be.back_end.enums.TshirtsEnums;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TshirtsDTO {
    private String name;
    private String description;
    private String AccountId;
    private String imageUrl;
    private int quantity;
}
