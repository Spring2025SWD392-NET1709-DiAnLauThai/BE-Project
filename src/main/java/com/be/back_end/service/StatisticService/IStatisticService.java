package com.be.back_end.service.StatisticService;

import com.be.back_end.dto.response.DashboardStatResponse;
import com.be.back_end.dto.response.MonthlyIncomeResponse;
import com.be.back_end.dto.response.MonthlyIncomeSummary;
import com.be.back_end.dto.response.YearlyIncomeResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public interface IStatisticService {

    DashboardStatResponse GetDashboardStatistics(LocalDate startDate, LocalDate endDate, int year);
    BigDecimal calculateTotalIncomeByDate(LocalDate date);
    MonthlyIncomeResponse calculateMonthlyIncome(int year, Month month);
    YearlyIncomeResponse calculateYearlyIncome(int year);
    List<MonthlyIncomeSummary> calculateMonthlyIncomeBasedOnDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
