package com.be.back_end.dto.request;

import com.be.back_end.model.Orders;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionRequest {
    private Orders orders;
    private String payment_name;
    private String payment_method;
    private LocalDateTime payment_date;
    private String payment_amount;
    private String order_info;
    private String order_type;
}
