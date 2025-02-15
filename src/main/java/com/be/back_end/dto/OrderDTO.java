package com.be.back_end.dto;

import com.be.back_end.enums.OrderEnums;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDTO {
    private String Id;
    private BigDecimal total_price;
    private int total_quantity;
    private OrderEnums status;
    private String order_notes;
}
