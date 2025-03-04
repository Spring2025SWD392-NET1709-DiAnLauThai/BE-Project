
package com.be.back_end.service.TranscationService;

import com.be.back_end.dto.BookingDTO;
import com.be.back_end.dto.TranscationDTO;

import com.be.back_end.dto.response.TransactionResponse;
import com.be.back_end.model.Account;
import com.be.back_end.model.Bookings;
import com.be.back_end.model.Transaction;


import com.be.back_end.repository.AccountRepository;
import com.be.back_end.repository.BookingRepository;
import com.be.back_end.repository.TranscationRepository;
import com.be.back_end.utils.VNPayUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class TranscationService implements ITranscationService, IVNPayService {

    private final TranscationRepository transcationRepository;
    private final VNPayUtils vnPayUtils;
    private final BookingRepository bookingRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public TranscationService(TranscationRepository transcationRepository, VNPayUtils vnPayUtils, BookingRepository bookingRepository, AccountRepository accountRepository) {
        this.transcationRepository = transcationRepository;
        this.vnPayUtils=vnPayUtils;
        this.bookingRepository = bookingRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public TranscationDTO create(TranscationDTO dto) {
        Transaction newTransaction = mapToEntity(dto);
        transcationRepository.save(newTransaction);
        return dto;
    }

    @Override
    public List<TranscationDTO> getAll() {
        List<Transaction> transactions = transcationRepository.findAll();
        List<TranscationDTO> list = new ArrayList<>();
        for (Transaction transaction : transactions) {
            list.add(mapToDTO(transaction));
            System.out.println(transaction.getId());
        }
        return list;
    }

    @Override
    public TranscationDTO getById(String id) {
        Transaction transaction = transcationRepository.findById(id).orElse(null);
        return mapToDTO(transaction);
    }


    @Override
    public List<TranscationDTO> getAllForCustomer(String id) {


            //Get a Customer
            Account account = accountRepository.findById(id).orElse(null);

            if (account != null) {

                String accountId = account.getId();
                List<Bookings> bookingsList = bookingRepository.findByaccount(account);
                if (bookingsList.isEmpty()) {
                    System.out.println("Booking list is empty");
                    return null;
                }

                //Get Transcation for every booking available
                List<Transaction> transactionList = null;
                for (Bookings Bookings : bookingsList) {
                    transactionList = transcationRepository.findAllById(Collections.singleton(Bookings.getId()));

                }

                if (transactionList.isEmpty()) {
                    System.out.println("Transcation list is empty");
                    return null;
                }

                //Conver them to DTO
                List<TranscationDTO> transcationDTOList = null;
                for (Transaction transaction : transactionList) {
                    TranscationDTO transcationDTO = mapToDTO(transaction);
                    transcationDTOList.add(transcationDTO);
                }

                if (transcationDTOList.isEmpty()) {
                    System.out.println("TranscationDTO list is empty");
                    return null;
                }

                return transcationDTOList;

            }
            System.out.println("Customer is empty");
            return null;

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


    public TransactionResponse createPaymentUrl(String amount, String orderInfo, String orderType, HttpServletRequest request) {
        try{
            String paymentUrl = vnPayUtils.generatePaymentUrl(amount, orderInfo, orderType, request);
            return TransactionResponse.builder()
                    .code("200")
                    .message("Tao link payment url successful")
                    .paymentUrl(paymentUrl).build();

        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
            return TransactionResponse.builder()
                    .code("400")
                    .message("Tao link payment url that bai do Exception")
                    .build();
        }

    }



}

