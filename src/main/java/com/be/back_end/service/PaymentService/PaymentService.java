/*
package com.be.back_end.service.PaymentService;

import com.be.back_end.dto.PaymentDTO;

import com.be.back_end.model.Transaction;

import com.be.back_end.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class PaymentService implements IPaymentService{

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentDTO create(PaymentDTO dto) {
        Transaction newTransaction = mapToEntity(dto);
        paymentRepository.save(newTransaction);
        return dto;
    }

    @Override
    public List<PaymentDTO> getAll() {
        List<Transaction> transactions = paymentRepository.findAll();
        List<PaymentDTO> list= new ArrayList<>();
        for(Transaction transaction : transactions)
        {
            list.add(mapToDTO(transaction));
            System.out.println(transaction.getId());
        }
        return list;
    }

    @Override
    public PaymentDTO getById(String id) {
        Transaction transaction = paymentRepository.findById(id).orElse(null);
        return mapToDTO(transaction);
    }

    @Override
    public boolean update(String id, PaymentDTO user) {
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


    private PaymentDTO mapToDTO(Transaction Transaction) {
        PaymentDTO dto = new PaymentDTO();
        dto.setBookings(Transaction.getOrders());
        dto.setPayment_date(Transaction.getPayment_date());
        dto.setPayment_amount(Transaction.getPayment_amount());
        dto.setPayment_method(Transaction.getPayment_method());
        dto.setPayment_name(Transaction.getPayment_name());
        return dto;
    }

    private Transaction mapToEntity(PaymentDTO dto) {
        Transaction transaction = new Transaction();
        transaction.setOrders(dto.getBookings());
        transaction.setPayment_date(dto.getPayment_date());
        transaction.setPayment_amount(dto.getPayment_amount());
        transaction.setPayment_method(dto.getPayment_method());
        transaction.setPayment_name(dto.getPayment_name());
        return transaction;
    }
}
*/
