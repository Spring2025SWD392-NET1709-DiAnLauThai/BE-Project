
package com.be.back_end.controller;

import com.be.back_end.dto.AccountDTO;
import com.be.back_end.dto.TransactionDTO;

import com.be.back_end.dto.request.TransactionRequest;
import com.be.back_end.dto.response.*;
import com.be.back_end.enums.ActivationEnums;
import com.be.back_end.enums.RoleEnums;
import com.be.back_end.service.TranscationService.ITranscationService;

import com.be.back_end.service.TranscationService.IVNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transcations")
public class TranscationController {

    private final ITranscationService transcationService;
    private final IVNPayService vnpayService;


    public TranscationController(ITranscationService transcationService, IVNPayService vnpayService) {
        this.transcationService = transcationService;
        this.vnpayService = vnpayService;
    }



    @PostMapping
    public ResponseEntity<TransactionDTO> createTranscation(@RequestBody TransactionDTO TransactionDTO) {
        TransactionDTO createdDesign = transcationService.create(TransactionDTO);
        System.out.println("Transcation created successfully.");
        return ResponseEntity.ok(createdDesign);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getTranscationById(@PathVariable String id) {
        TransactionDTO design = transcationService.getById(id);
        if (design == null) {
            return ResponseEntity.badRequest().body("Transcation not found with ID: " + id);
        }
        return ResponseEntity.ok(design);
    }




    //Get all transaction belong to Customer, via Bookings
    //Input is customer id, then get all booking, then get all transaction in each bookings
    @GetMapping("/customer/{id}")
    public ResponseEntity<?> getTranscationForCustomer(
            @PathVariable String id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "transactionDate") String sortBy) {
        if (page < 0 || size <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponse(400, null, List.of("Page and size must be positive values")));
        }
        PaginatedResponseDTO<TransactionDTO> transactions = transcationService.getAllByCustomer(id,page,size,sortDir,sortBy);
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
        PaginatedResponseDTO<TransactionDTO> transactions = transcationService.getAll(
                 page, size, sortDir, sortBy);
        if (transactions.getContent().isEmpty()) {
            return ResponseEntity.status(204).body(new ApiResponse<>(204, null, "No data available"));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, transactions, "Page returned: " + page));
    }



    //Get transaction Detail with transaction id
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getTransactionDetail(@PathVariable String id) {
        TransactionDetailResponse transactionDetail = transcationService.getTransactionDetail(id);
        if (transactionDetail == null) {
            return ResponseEntity.badRequest().body("Failed to create detail response ");
        }
        return ResponseEntity.ok(transactionDetail);
    }


    @GetMapping("/makePayment")
    public ResponseEntity<TransactionResponse> makePayment(@RequestBody TransactionRequest transactionRequest, HttpServletRequest request) {
        String amount = transactionRequest.getPayment_amount();
        String orderInfo = transactionRequest.getBooking_info();
        String orderType = transactionRequest.getBooking_type();
        TransactionResponse response = vnpayService.createPaymentUrl(amount, orderInfo, orderType,request);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/callback")
    public ResponseEntity<?> payCallbackHandler(HttpServletRequest request, HttpServletResponse response) {
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


    /*
    //Get all transcations belong to Customer, via Bookings
    //Input is customer id, then get all booking, then get all transcation in each bookings
    //Might need to recreate customer response
    @GetMapping("/customer/{id}")
    public ResponseEntity<?> getTranscationForCustomer(@PathVariable String id) throws Exception {
        List<TransactionDTO> transcations = transcationService.getAllForCustomer(id);
        if (transcations.isEmpty()) {
            System.out.println("No Transcation found.");
        }
        return ResponseEntity.ok(transcations);
    }
    */
}

