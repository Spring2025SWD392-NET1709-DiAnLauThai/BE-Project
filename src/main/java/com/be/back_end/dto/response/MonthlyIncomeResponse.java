package com.be.back_end.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Month;
import java.util.List;

@Data
public class MonthlyIncomeResponse {
    private Month month;
    private int year;
    private BigDecimal totalIncome;
    private List<DailyIncomeResponse> dailyIncome;

    public MonthlyIncomeResponse(Month month, int year, BigDecimal totalIncome, List<DailyIncomeResponse> dailyIncome) {
        this.month = month;
        this.year = year;
        this.totalIncome = totalIncome;
        this.dailyIncome = dailyIncome;
    }
}
