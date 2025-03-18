
package com.be.back_end.controller;


import com.be.back_end.dto.request.TransactionRequest;
import com.be.back_end.dto.response.*;
import com.be.back_end.service.TranscationService.ITransactionService;

import com.be.back_end.service.TranscationService.IVNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final ITransactionService transactionService;
    private final IVNPayService vnpayService;


    public TransactionController(ITransactionService transactionService, IVNPayService vnpayService) {
        this.transactionService = transactionService;
        this.vnpayService = vnpayService;
    }



    @PostMapping
    public ResponseEntity<TransactionDTO> createTranscation(@RequestBody TransactionDTO TransactionDTO) {
        TransactionDTO createdDesign = transactionService.create(TransactionDTO);
        System.out.println("Transcation created successfully.");
        return ResponseEntity.ok(createdDesign);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getTranscationById(@PathVariable String id) {
        TransactionDTO design = transactionService.getById(id);
        if (design == null) {
            return ResponseEntity.badRequest().body("Transcation not found with ID: " + id);
        }
        return ResponseEntity.ok(design);
    }




    //Get all transaction belong to Customer, via Bookings
    //Input is customer id, then get all booking, then get all transaction in each bookings
    @GetMapping("/customer")
    public ResponseEntity<?> getTranscationForCustomer(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "transactionDate") String sortBy) {
        if (page < 0 || size <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponse(400, null, List.of("Page and size must be positive values")));
        }
        PaginatedResponseDTO<TransactionDTO> transactions = transactionService.getAllByCustomer(page,size,sortDir,sortBy);
        if (transactions.getContent().isEmpty()) {
            return ResponseEntity.status(204).body(new ApiResponse<>(204, null, "No data available"));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, transactions, "Page returned: " + page));
    }


    //Get all transaction with pagination
    @GetMapping("/system")
    public ResponseEntity<?> getTransaction(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "transactionDate") String sortBy
    ) {
        if (page < 0 || size <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponse(400, null, List.of("Page and size must be positive values")));
        }
        PaginatedResponseDTO<TransactionDTO> transactions = transactionService.getAll(
                 page, size, sortDir, sortBy);
        if (transactions.getContent().isEmpty()) {
            return ResponseEntity.status(204).body(new ApiResponse<>(204, null, "No data available"));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, transactions, "Page returned: " + page));
    }



    //Get transaction Detail with transaction id
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getTransactionDetail(@PathVariable String id) {
        TransactionDetailResponse transactionDetail = transactionService.getTransactionDetail(id);
        if (transactionDetail == null) {
            return ResponseEntity.ok(new ErrorResponse(400, "Failed to get detail", null));
        }
        return ResponseEntity.ok(new ApiResponse(200, transactionDetail, "Transaction detail returned"));
    }


    @GetMapping("/makePayment")
    public ResponseEntity<TransactionResponse> makePayment(@RequestBody TransactionRequest transactionRequest, HttpServletRequest request) {
        String amount = transactionRequest.getPayment_amount();
        String orderInfo = transactionRequest.getBooking_info();
        String orderType = transactionRequest.getBooking_type();
        TransactionResponse response = vnpayService.createPaymentUrl(amount, orderInfo, orderType,request);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/callback/fully_paid")
    public ResponseEntity<?> payCallbackHandlerPayment(HttpServletRequest request, HttpServletResponse response) {
        try {
            String transactionStatus = vnpayService.processPaymentCallback(request);
            String redirectUrl = "http://localhost:3000/success-booking";
            String redirectWithParams = redirectUrl + "?status=" + transactionStatus +
                    "&message=" + (transactionStatus.equals("SUCCESS")
                    ? "Payment successful"
                    : "Payment failed");
            response.sendRedirect(redirectWithParams);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, transactionStatus,
                            transactionStatus.equals("SUCCESS") ? "Payment successful" : "Payment failed")
            );
        } catch (Exception e) {
            try {
                response.sendRedirect("http://localhost:3000/success-booking?status=FAILED&message=Transaction%20processing%20failed");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "Transaction failed", List.of(e.getMessage())));
        }
    }


    @GetMapping("/callback/deposited")
    public ResponseEntity<?> payCallbackHandlerDeposit(HttpServletRequest request, HttpServletResponse response) {
        try {
            String transactionStatus = vnpayService.processDepositCallback(request);
            String redirectUrl = "http://localhost:3000/success-booking";
            String redirectWithParams = redirectUrl + "?status=" + transactionStatus +
                    "&message=" + (transactionStatus.equals("SUCCESS")
                    ? "Payment successful"
                    : "Payment failed");
            response.sendRedirect(redirectWithParams);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, transactionStatus,
                            transactionStatus.equals("SUCCESS") ? "Payment successful" : "Payment failed")
            );
        } catch (Exception e) {
            try {
                response.sendRedirect("http://localhost:3000/success-booking?status=FAILED&message=Transaction%20processing%20failed");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "Transaction failed", List.of(e.getMessage())));
        }
    }

    @GetMapping("/daily-income")
    public ResponseEntity<DailyIncomeResponse> getDailyIncome(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        BigDecimal totalIncome = transactionService.calculateTotalIncomeByDate(date);
        DailyIncomeResponse response = new DailyIncomeResponse(date, totalIncome);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/monthly-income")
    public ResponseEntity<MonthlyIncomeResponse> getMonthlyIncome(
            @RequestParam int year,
            @RequestParam int month) {

        // Validate month input (1-12)
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }

        Month monthEnum = Month.of(month);
        MonthlyIncomeResponse response = transactionService.calculateMonthlyIncome(year, monthEnum);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/yearly-income")
    public ResponseEntity<YearlyIncomeResponse> getYearlyIncome(
            @RequestParam int year) {

        YearlyIncomeResponse response = transactionService.calculateYearlyIncome(year);

        return ResponseEntity.ok(response);
    }


}

