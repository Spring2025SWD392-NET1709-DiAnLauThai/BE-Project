package com.be.back_end.dto;

import com.be.back_end.model.Orders;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionDTO {
    private Orders orders;
    private String payment_name;
    private String payment_method;
    private LocalDateTime payment_date;
    private BigDecimal payment_amount;
}
