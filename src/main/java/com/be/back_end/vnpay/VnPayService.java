package com.be.back_end.vnpay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VnPayService {


    @Value("${vnpay.tmnCode}")
    private String vnp_TmnCode;

    @Value("${vnpay.hashSecret}")
    private String vnp_HashSecret;

    @Value("${vnpay.payUrl}")
    private String vnp_PayUrl;

    @Value("${vnpay.returnUrl}")
    private String vnp_ReturnUrl;

    public String createPaymentUrl(long amount, String orderInfo) {
        return VnPayUtil.generatePaymentUrl(vnp_TmnCode, vnp_HashSecret, vnp_PayUrl, vnp_ReturnUrl, amount, orderInfo);
    }
}
