package com.be.back_end.controller;

import com.be.back_end.dto.PaymentDTO;
import com.be.back_end.dto.request.PaymentRequest;
import com.be.back_end.vnpay.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/vnpay")
public class VNPayController {

    private final VnPayService vnpayService;

    public VNPayController(VnPayService vnpayService) {
        this.vnpayService = vnpayService;
    }

    @GetMapping("/makePayment")
    public String createPayment(@RequestBody PaymentRequest paymentRequest, HttpServletRequest request) throws UnsupportedEncodingException {
        String amount = paymentRequest.getPayment_amount();
        String orderInfo = paymentRequest.getOrder_info();
        String orderType = paymentRequest.getOrder_type();
        String paymentUrl = vnpayService.createPaymentUrl(amount, orderInfo, orderType,request);
        return paymentUrl;
    }
}
