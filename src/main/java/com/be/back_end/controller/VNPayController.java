package com.be.back_end.controller;

import com.be.back_end.dto.request.TransactionRequest;
import com.be.back_end.dto.response.TransactionResponse;
import com.be.back_end.vnpay.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/vnpay")
public class VNPayController {

    private final VnPayService vnpayService;

    public VNPayController(VnPayService vnpayService) {
        this.vnpayService = vnpayService;
    }

    @GetMapping("/makePayment")
    public ResponseEntity<TransactionResponse> createPayment(@RequestBody TransactionRequest transactionRequest, HttpServletRequest request) throws UnsupportedEncodingException {
        String amount = transactionRequest.getPayment_amount();
        String orderInfo = transactionRequest.getOrder_info();
        String orderType = transactionRequest.getOrder_type();
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
