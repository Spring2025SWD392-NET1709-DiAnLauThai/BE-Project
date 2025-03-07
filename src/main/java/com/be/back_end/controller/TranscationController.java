
package com.be.back_end.controller;

import com.be.back_end.dto.TransactionDTO;

import com.be.back_end.dto.request.TransactionRequest;
import com.be.back_end.dto.response.ApiResponse;
import com.be.back_end.dto.response.ErrorResponse;
import com.be.back_end.dto.response.TransactionDetailResponse;
import com.be.back_end.dto.response.TransactionResponse;
import com.be.back_end.service.TranscationService.ITranscationService;

import com.be.back_end.service.TranscationService.IVNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    //Normal Get All
    @GetMapping("/system")
    public ResponseEntity<?> getTranscationForSystem() {
        List<TransactionDTO> payments = transcationService.getAll();
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
        List<TransactionDTO> transcations = transcationService.getAllForCustomer(id);
        if (transcations.isEmpty()) {
            System.out.println("No Transcation found.");
        }
        return ResponseEntity.ok(transcations);
    }


    //Not Yet Completed
    //Tham vao la ID cua booking de lay Transaction va BookingDetail
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
    public ResponseEntity<?> payCallbackHandler(HttpServletRequest request) {
        try {
            String transactionStatus = vnpayService.processPaymentCallback(request);
            return ResponseEntity.ok(new ApiResponse<>(200, transactionStatus,
                    transactionStatus.equals("SUCCESS") ? "Payment successful" : "Payment failed"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "Transaction processing failed", List.of(e.getMessage())));
        }
    }

}

