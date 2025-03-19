package com.be.back_end.utils.Mapper;

import com.be.back_end.dto.response.TransactionDTO;
import com.be.back_end.model.Bookings;
import com.be.back_end.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {



    public TransactionDTO mapToDTO(Transaction Transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(Transaction.getId());
        dto.setBookingId(Transaction.getBooking().getId());
        dto.setTransactionName(Transaction.getTransactionName());
        dto.setTransactionMethod(Transaction.getTransactionMethod());
        dto.setTransactionDate(Transaction.getTransactionDate());
        dto.setTransactionStatus(Transaction.getTransactionStatus());
        dto.setTransactionAmount(Transaction.getTransactionAmount());
        dto.setBankCode(Transaction.getBankCode());
        return dto;
    }


    public Transaction mapToEntity(TransactionDTO dto, Bookings bookings) {
        Transaction transaction = new Transaction();
        transaction.setId(dto.getId());
        transaction.setBooking(bookings);
        transaction.setTransactionName(dto.getTransactionName());
        transaction.setTransactionMethod(dto.getTransactionMethod());
        transaction.setTransactionDate(dto.getTransactionDate());
        transaction.setTransactionStatus(dto.getTransactionStatus());
        transaction.setTransactionAmount(dto.getTransactionAmount());
        transaction.setBankCode(dto.getBankCode());
        return transaction;
    }
}
