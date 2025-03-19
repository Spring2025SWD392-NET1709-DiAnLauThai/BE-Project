
package com.be.back_end.service.TranscationService;




import com.be.back_end.dto.response.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

public interface ITransactionService {
    TransactionDTO create(TransactionDTO user);
    PaginatedResponseDTO<TransactionDTO> getAll(int page, int size, String sortDir, String sortBy);
    TransactionDTO getById(String id);
    PaginatedResponseDTO<TransactionDTO> getAllByCustomer(int page, int size, String sortDir, String sortBy);
    TransactionDetailResponse getTransactionDetail(String id);


}

