package com.be.back_end.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class YearlyIncomeResponse {
    private int year;
    private BigDecimal totalIncome;
    private List<MonthlyIncomeSummary> monthlyIncomes;

    public YearlyIncomeResponse(int year, BigDecimal totalIncome, List<MonthlyIncomeSummary> monthlyIncomes) {
        this.year = year;
        this.totalIncome = totalIncome;
        this.monthlyIncomes = monthlyIncomes;
    }
}
