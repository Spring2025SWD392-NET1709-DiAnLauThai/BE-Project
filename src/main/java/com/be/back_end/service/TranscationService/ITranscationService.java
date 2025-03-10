
package com.be.back_end.service.TranscationService;

import com.be.back_end.dto.TransactionDTO;
import com.be.back_end.dto.response.PaginatedResponseDTO;
import com.be.back_end.dto.response.TransactionDetailResponse;

import java.util.List;

public interface ITranscationService {
    TransactionDTO create(TransactionDTO user);
    PaginatedResponseDTO<TransactionDTO> getAll(int page, int size, String sortDir, String sortBy);
    TransactionDTO getById(String id);
    PaginatedResponseDTO<TransactionDTO> getAllByCustomer(String customerId, int page, int size, String sortDir, String sortBy);
    TransactionDetailResponse getTransactionDetail(String id);
}

