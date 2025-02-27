
package com.be.back_end.controller;

import com.be.back_end.dto.TranscationDTO;

import com.be.back_end.dto.request.TransactionRequest;
import com.be.back_end.dto.response.TransactionResponse;
import com.be.back_end.service.TranscationService.ITranscationService;

import com.be.back_end.service.TranscationService.IVNPayService;
import jakarta.servlet.http.HttpServletRequest;
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

    @GetMapping
    public ResponseEntity<List<TranscationDTO>> getAllTranscation() {
        List<TranscationDTO> payments = transcationService.getAll();
        if (payments.isEmpty()) {
            System.out.println("No Transcation found.");
        }
        return ResponseEntity.ok(payments);
    }

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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTranscation(@PathVariable String id, @RequestBody TranscationDTO TranscationDTO) {
        boolean updated = transcationService.update(id, TranscationDTO);
        if (!updated) {
            return ResponseEntity.badRequest().body("Failed to update. Transcation not found with ID: " + id);
        }
        return ResponseEntity.ok("Transcation updated successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTranscation(@PathVariable String id) {
        boolean deleted = transcationService.delete(id);
        if (!deleted) {
            return ResponseEntity.badRequest().body("Failed to delete. Transcation not found with ID: " + id);
        }
        return ResponseEntity.ok("Transcation deleted successfully.");
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
    }

}

