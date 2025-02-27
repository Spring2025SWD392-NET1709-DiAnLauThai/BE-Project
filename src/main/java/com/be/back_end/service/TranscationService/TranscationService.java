
package com.be.back_end.service.TranscationService;

import com.be.back_end.dto.TranscationDTO;

import com.be.back_end.model.Transaction;


import com.be.back_end.repository.TranscationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TranscationService implements ITranscationService {

    private final TranscationRepository paymentRepository;

    @Autowired
    public TranscationService(TranscationRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public TranscationDTO create(TranscationDTO dto) {
        Transaction newTransaction = mapToEntity(dto);
        paymentRepository.save(newTransaction);
        return dto;
    }

    @Override
    public List<TranscationDTO> getAll() {
        List<Transaction> transactions = paymentRepository.findAll();
        List<TranscationDTO> list= new ArrayList<>();
        for(Transaction transaction : transactions)
        {
            list.add(mapToDTO(transaction));
            System.out.println(transaction.getId());
        }
        return list;
    }

    @Override
    public TranscationDTO getById(String id) {
        Transaction transaction = paymentRepository.findById(id).orElse(null);
        return mapToDTO(transaction);
    }

    @Override
    public boolean update(String id, TranscationDTO user) {
        Transaction updatedTransaction = paymentRepository.findById(id).orElse(null);
        if(updatedTransaction ==null){
            return false;
        }
        updatedTransaction =mapToEntity(user);
        paymentRepository.save(updatedTransaction);
        return true;
    }

    @Override
    public boolean delete(String id) {
        Transaction existingTransaction = paymentRepository.getById(id);
        if (existingTransaction != null) {
            paymentRepository.delete(existingTransaction);
            return true;
        }
        return false;
    }


    private TranscationDTO mapToDTO(Transaction Transaction) {
        TranscationDTO dto = new TranscationDTO();
        dto.setBookings(Transaction.getBookings());
        dto.setPayment_date(Transaction.getTransactionDate());
        dto.setPayment_amount(Transaction.getTransactionAmount());
        dto.setPayment_method(Transaction.getTransactionMethod());
        dto.setPayment_name(Transaction.getTransactionName());
        return dto;
    }

    private Transaction mapToEntity(TranscationDTO dto) {
        Transaction transaction = new Transaction();
        transaction.setBookings(dto.getBookings());
        transaction.setTransactionDate(dto.getPayment_date());
        transaction.setTransactionAmount(dto.getPayment_amount());
        transaction.setTransactionMethod(dto.getPayment_method());
        transaction.setTransactionName(dto.getPayment_name());
        return transaction;
    }
}

