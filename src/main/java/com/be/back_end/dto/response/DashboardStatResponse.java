package com.be.back_end.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DashboardStatResponse {
    private int year;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private List<MonthlyIncomeSummary> monthlyIncome;
    private List<MonthyAmountResponse> tShirtCreatedAmount;
    private List<MonthyAmountResponse> bookingCreatedAmount;
    private List<MonthyAmountResponse> bookingCompletedAmount;
}
