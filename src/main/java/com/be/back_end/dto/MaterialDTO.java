package com.be.back_end.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MaterialDTO {
    private String id;
    private String name;
    private BigDecimal price;
    private int quantity;
}
