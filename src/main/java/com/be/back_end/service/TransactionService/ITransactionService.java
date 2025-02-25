package com.be.back_end.service.TransactionService;

import com.be.back_end.dto.TransactionDTO;

import java.util.List;

public interface ITransactionService {
    TransactionDTO create(TransactionDTO user);
    List<TransactionDTO> getAll();
    TransactionDTO getById(String id);
    boolean update(String id, TransactionDTO user);
    boolean delete(String id);
}
