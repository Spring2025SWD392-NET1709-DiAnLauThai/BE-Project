package com.be.back_end.service.TranscationService;

import com.be.back_end.dto.response.TransactionResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface IVNPayService {
    public TransactionResponse createPaymentUrl(String amount, String orderInfo, String orderType, HttpServletRequest request);

}
