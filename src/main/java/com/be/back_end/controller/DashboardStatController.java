package com.be.back_end.controller;

import com.be.back_end.dto.response.*;
import com.be.back_end.service.StatisticService.IStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard-stat")
public class DashboardStatController {

    private final IStatisticService statisticService;

    @Autowired
    public DashboardStatController(IStatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("/daily-income")
    public ResponseEntity<?> getDailyIncome(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        BigDecimal totalIncome = statisticService.calculateTotalIncomeByDate(date);
        DailyIncomeResponse response = new DailyIncomeResponse(date, totalIncome);

        if(response==null){
            return ResponseEntity.status(400).body(new ErrorResponse(400, "Failed to get data", List.of("Daily income not found")));
        }

        return ResponseEntity.ok(new ApiResponse<>(200, response, "Daily income retrieved successfully"));
    }

    @GetMapping("/monthly-income")
    public ResponseEntity<?> getMonthlyIncome(
            @RequestParam int year,
            @RequestParam int month) {

        // Validate month input (1-12)
        if (month < 1 || month > 12) {
            return ResponseEntity.status(400).body(new ErrorResponse(400, "Invalid month data", List.of("Month must be between 1 and 12")));
        }

        Month monthEnum = Month.of(month);
        MonthlyIncomeResponse response = statisticService.calculateMonthlyIncome(year, monthEnum);

        if(response==null){
            return ResponseEntity.status(400).body(new ErrorResponse(400, "Failed to get data", List.of("Monthly income not found")));
        }

        return ResponseEntity.ok(new ApiResponse<>(200,response, "Monthly income retrieved successfully"));
    }

    @GetMapping("/yearly-income")
    public ResponseEntity<?> getYearlyIncome(
            @RequestParam int year) {

        YearlyIncomeResponse response = statisticService.calculateYearlyIncome(year);

        if(response==null){
            return ResponseEntity.status(400).body(new ErrorResponse(400, "Failed to get data", List.of("Yearly income not found")));
        }

        return ResponseEntity.ok(new ApiResponse<>(200, response, "Yearly income retrieved successfully"));
    }

    @GetMapping("/general-stat")
    public ResponseEntity<?> getGeneralStat(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam int year) {

        DashboardStatResponse response = statisticService.GetDashboardStatistics(startDate, endDate, year);

        if(response==null){
            return ResponseEntity.status(400).body(new ErrorResponse(400, "Failed to get data", List.of("General statistics can't be created due to inner error")));
        }

        return ResponseEntity.ok(new ApiResponse<>(200, response, "General statistics retrieved and return successfully"));
    }
}
