package com.be.back_end.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DailyIncomeResponse {
    private LocalDate date;
    private BigDecimal totalIncome;

    public DailyIncomeResponse(LocalDate date, BigDecimal totalIncome) {
        this.date = date;
        this.totalIncome = totalIncome;
    }
}
