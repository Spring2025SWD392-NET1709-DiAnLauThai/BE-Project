package com.be.back_end.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Month;

@Data
//Used for count amount of object of each month (Can be applied for most of the countable entity)
public class MonthyAmountResponse {
    private Month month;
    private long amount;

    public MonthyAmountResponse(Month month, long amount) {
        this.month = month;
        this.amount = amount;
    }
}
