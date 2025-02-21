package com.be.back_end.dto;

import com.be.back_end.enums.ActivationEnums;
import com.be.back_end.enums.BookingEnums;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookingDTO {
    private String Id;
    private BigDecimal total_price;
    private int total_quantity;
    private BookingEnums status;
    private String order_notes;
}
