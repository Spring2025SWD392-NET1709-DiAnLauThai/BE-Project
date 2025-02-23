package com.be.back_end.vnpay;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

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

    public String createPaymentUrl(String amount, String orderInfo, String orderType, HttpServletRequest request) throws UnsupportedEncodingException {
        return VnPayUtil.generatePaymentUrl(vnp_TmnCode, vnp_HashSecret, vnp_PayUrl, vnp_ReturnUrl, amount, orderInfo, orderType, request);
    }
}
