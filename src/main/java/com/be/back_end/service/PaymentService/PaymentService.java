package com.be.back_end.service.PaymentService;

import com.be.back_end.dto.AccountDTO;
import com.be.back_end.dto.PaymentDTO;

import com.be.back_end.model.Account;
import com.be.back_end.model.Payment;

import com.be.back_end.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService implements IPaymentService{

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentDTO create(PaymentDTO dto) {
        Payment newPayment= mapToEntity(dto);
        paymentRepository.save(newPayment);
        return dto;
    }

    @Override
    public List<PaymentDTO> getAll() {
        List<Payment> payments= paymentRepository.findAll();
        List<PaymentDTO> list= new ArrayList<>();
        for(Payment payment:payments)
        {
            list.add(mapToDTO(payment));
            System.out.println(payment.getId());
        }
        return list;
    }

    @Override
    public PaymentDTO getById(String id) {
        Payment payment= paymentRepository.findById(id).orElse(null);
        return mapToDTO(payment);
    }

    @Override
    public boolean update(String id, PaymentDTO user) {
        Payment updatedPayment= paymentRepository.findById(id).orElse(null);
        if(updatedPayment==null){
            return false;
        }
        updatedPayment=mapToEntity(user);
        paymentRepository.save(updatedPayment);
        return true;
    }

    @Override
    public boolean delete(String id) {
        Payment existingPayment = paymentRepository.getById(id);
        if (existingPayment != null) {
            paymentRepository.delete(existingPayment);
            return true;
        }
        return false;
    }


    private PaymentDTO mapToDTO(Payment Payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setOrders(Payment.getOrders());
        dto.setPayment_date(Payment.getPayment_date());
        dto.setPayment_amount(Payment.getPayment_amount());
        dto.setPayment_method(Payment.getPayment_method());
        dto.setPayment_name(Payment.getPayment_name());
        return dto;
    }

    private Payment mapToEntity(PaymentDTO dto) {
        Payment payment = new Payment();
        payment.setOrders(dto.getOrders());
        payment.setPayment_date(dto.getPayment_date());
        payment.setPayment_amount(dto.getPayment_amount());
        payment.setPayment_method(dto.getPayment_method());
        payment.setPayment_name(dto.getPayment_name());
        return payment;
    }
}
