package com.be.back_end.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Data
@Builder
public class CustomerMonthlyAmount {
    private int year;
    private Month startMonth;
    private Month endMonth;

    private List<MonthyAmountResponse> customerMonthlyAmount;
}
