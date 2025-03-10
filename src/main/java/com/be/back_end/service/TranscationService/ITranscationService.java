
package com.be.back_end.service.TranscationService;

import com.be.back_end.dto.TransactionDTO;
import com.be.back_end.dto.response.TransactionDetailResponse;

import java.util.List;

public interface ITranscationService {
    TransactionDTO create(TransactionDTO user);
    List<TransactionDTO> getAll();
    TransactionDTO getById(String id);
    List<TransactionDTO> getAllForCustomer(String id);
    TransactionDetailResponse getTransactionDetail(String id);
}

