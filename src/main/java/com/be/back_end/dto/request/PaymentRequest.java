package com.be.back_end.dto.request;

import com.be.back_end.model.Orders;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentRequest {
    private Orders orders;
    private String payment_name;
    private String payment_method;
    private LocalDateTime payment_date;
    private long payment_amount;
    private String order_info;
    private String order_type;
}
