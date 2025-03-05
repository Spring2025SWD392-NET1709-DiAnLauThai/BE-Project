package com.be.back_end.service.TranscationService;

import com.be.back_end.dto.response.TransactionResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface IVNPayService {
    public TransactionResponse createPaymentUrl(String amount, String orderInfo, String orderType, HttpServletRequest request);



     String processPaymentCallback(HttpServletRequest request);
}
