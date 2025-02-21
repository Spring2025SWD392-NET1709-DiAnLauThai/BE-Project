package com.be.back_end.controller;

import com.be.back_end.dto.PaymentDTO;
import com.be.back_end.vnpay.VnPayService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/vnpay")
public class VNPayController {

    private final VnPayService vnpayService;

    public VNPayController(VnPayService vnpayService) {
        this.vnpayService = vnpayService;
    }

    @GetMapping("/makePayment")
    public String createPayment(@RequestBody PaymentDTO paymentDTO, @RequestParam String orderInfo) {
        String amount = paymentDTO.getPayment_amount().toString();
        long newAmount = Long.parseLong(amount);
        String paymentUrl = vnpayService.createPaymentUrl(newAmount, orderInfo);
        return paymentUrl;
    }
}
