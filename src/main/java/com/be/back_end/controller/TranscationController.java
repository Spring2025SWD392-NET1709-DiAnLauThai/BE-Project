
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
import java.util.Map;

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
    public ResponseEntity<TranscationDTO> createTranscation(@RequestBody TranscationDTO TranscationDTO) {
        TranscationDTO createdDesign = transcationService.create(TranscationDTO);
        System.out.println("Transcation created successfully.");
        return ResponseEntity.ok(createdDesign);
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

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getTranscationDetail(@PathVariable String id) {
        TranscationDTO design = transcationService.getById(id);
        if (design == null) {
            return ResponseEntity.badRequest().body("Transcation not found with ID: " + id);
        }
        return ResponseEntity.ok(design);
    }


    @PostMapping("/makePayment")
    public ResponseEntity<TransactionResponse> makePayment(@RequestBody TransactionRequest transactionRequest, HttpServletRequest request) {
        String amount = transactionRequest.getPayment_amount();
        String orderInfo = transactionRequest.getBooking_info();
        String orderType = transactionRequest.getTransactionType();
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

