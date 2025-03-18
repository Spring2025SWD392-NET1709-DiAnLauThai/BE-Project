package com.be.back_end.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Month;

@Data
public class MonthlyIncomeSummary {
    private Month month;
    private BigDecimal amount;

    public MonthlyIncomeSummary(Month month, BigDecimal amount) {
        this.month = month;
        this.amount = amount;
    }
}
