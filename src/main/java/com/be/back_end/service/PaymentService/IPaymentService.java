package com.be.back_end.service.PaymentService;

import com.be.back_end.dto.AccountDTO;
import com.be.back_end.dto.PaymentDTO;

import java.util.List;

public interface IPaymentService {
    PaymentDTO create(PaymentDTO user);
    List<PaymentDTO> getAll();
    PaymentDTO getById(String id);
    boolean update(String id,PaymentDTO user);
    boolean delete(String id);
}
