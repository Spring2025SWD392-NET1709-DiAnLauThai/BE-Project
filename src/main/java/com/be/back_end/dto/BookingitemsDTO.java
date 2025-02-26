package com.be.back_end.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookingitemsDTO {
    private String id;
    private int quantity;
    private BigDecimal unit_price;
}
