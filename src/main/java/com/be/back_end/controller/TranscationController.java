
package com.be.back_end.controller;

import com.be.back_end.dto.TranscationDTO;

import com.be.back_end.dto.request.TransactionRequest;
import com.be.back_end.dto.response.ApiResponse;
import com.be.back_end.dto.response.ErrorResponse;
import com.be.back_end.dto.response.TransactionResponse;
import com.be.back_end.service.TranscationService.ITranscationService;

import com.be.back_end.service.TranscationService.IVNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    /*
    @GetMapping
    public ResponseEntity<List<TranscationDTO>> getAllTranscation() {
        List<TranscationDTO> payments = transcationService.getAll();
        if (payments.isEmpty()) {
            System.out.println("No Transcation found.");
        }
        return ResponseEntity.ok(payments);
    }*/

    @PostMapping
    public ResponseEntity<TranscationDTO> createTranscation(@RequestBody TranscationDTO TranscationDTO) {
        TranscationDTO createdDesign = transcationService.create(TranscationDTO);
        System.out.println("Transcation created successfully.");
        return ResponseEntity.ok(createdDesign);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getTranscationById(@PathVariable String id) {
        TranscationDTO design = transcationService.getById(id);
        if (design == null) {
            return ResponseEntity.badRequest().body("Transcation not found with ID: " + id);
        }
        return ResponseEntity.ok(design);
    }

    //Normal Get All
    @GetMapping("/system")
    public ResponseEntity<?> getTranscationForSystem() {
        List<TranscationDTO> payments = transcationService.getAll();
        if (payments.isEmpty()) {
            System.out.println("No Transcation found.");
        }
        return ResponseEntity.ok(payments);
    }

    //Get all transcations belong to Customer, via Bookings
    //Input is customer id, then get all booking, then get all transcation in each bookings
    //Might need to recreate customer response
    @GetMapping("/customer/{id}")
    public ResponseEntity<?> getTranscationForCustomer(@PathVariable String id) throws Exception {
        List<TranscationDTO> transcations = transcationService.getAllForCustomer(id);
        if (transcations.isEmpty()) {
            System.out.println("No Transcation found.");
        }
        return ResponseEntity.ok(transcations);
    }


    //Not Yet Completed
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getTranscationDetail(@PathVariable String id) {
        TranscationDTO design = transcationService.getById(id);
        if (design == null) {
            return ResponseEntity.badRequest().body("Transcation not found with ID: " + id);
        }
        return ResponseEntity.ok(design);
    }


    @GetMapping("/makePayment")
    public ResponseEntity<TransactionResponse> makePayment(@RequestBody TransactionRequest transactionRequest, HttpServletRequest request) {
        String amount = transactionRequest.getPayment_amount();
        String orderInfo = transactionRequest.getBooking_info();
        String orderType = transactionRequest.getBooking_type();
        TransactionResponse response = vnpayService.createPaymentUrl(amount, orderInfo, orderType,request);
        return ResponseEntity.ok(response);
    }

    /*@GetMapping("/callback")
    public ResponseEntity<TransactionResponse> payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        if (status.equals("00")) {
            return ResponseEntity.ok(TransactionResponse.builder()
                    .code("00: Success")
                    .message("Giao dich thanh cong")
                    .build());
        } else {
            return ResponseEntity.badRequest().body(TransactionResponse.builder()
                    .code("99: Fail")
                    .message("Giao dich that bai")
                    .build());
        }
    }*/
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

}

