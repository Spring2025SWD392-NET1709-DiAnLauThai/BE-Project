package com.be.back_end.service.TransactionService;

import com.be.back_end.dto.TransactionDTO;

import com.be.back_end.model.Transaction;

import com.be.back_end.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService implements ITransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public TransactionDTO create(TransactionDTO dto) {
        Transaction newTransaction = mapToEntity(dto);
        transactionRepository.save(newTransaction);
        return dto;
    }

    @Override
    public List<TransactionDTO> getAll() {
        List<Transaction> transactions = transactionRepository.findAll();
        List<TransactionDTO> list= new ArrayList<>();
        for(Transaction transaction : transactions)
        {
            list.add(mapToDTO(transaction));
            System.out.println(transaction.getId());
        }
        return list;
    }

    @Override
    public TransactionDTO getById(String id) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        return mapToDTO(transaction);
    }

    @Override
    public boolean update(String id, TransactionDTO user) {
        Transaction updatedTransaction = transactionRepository.findById(id).orElse(null);
        if(updatedTransaction ==null){
            return false;
        }
        updatedTransaction =mapToEntity(user);
        transactionRepository.save(updatedTransaction);
        return true;
    }

    @Override
    public boolean delete(String id) {
        Transaction existingTransaction = transactionRepository.getById(id);
        if (existingTransaction != null) {
            transactionRepository.delete(existingTransaction);
            return true;
        }
        return false;
    }


    private TransactionDTO mapToDTO(Transaction Transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setOrders(Transaction.getOrders());
        dto.setPayment_date(Transaction.getPayment_date());
        dto.setPayment_amount(Transaction.getPayment_amount());
        dto.setPayment_method(Transaction.getPayment_method());
        dto.setPayment_name(Transaction.getPayment_name());
        return dto;
    }

    private Transaction mapToEntity(TransactionDTO dto) {
        Transaction transaction = new Transaction();
        transaction.setOrders(dto.getOrders());
        transaction.setPayment_date(dto.getPayment_date());
        transaction.setPayment_amount(dto.getPayment_amount());
        transaction.setPayment_method(dto.getPayment_method());
        transaction.setPayment_name(dto.getPayment_name());
        return transaction;
    }
}
