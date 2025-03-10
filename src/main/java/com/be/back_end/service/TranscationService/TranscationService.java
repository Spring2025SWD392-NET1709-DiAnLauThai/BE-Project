
package com.be.back_end.service.TranscationService;

import com.be.back_end.dto.TransactionDTO;

import com.be.back_end.dto.response.BookingDetailResponseDTO;
import com.be.back_end.dto.response.TransactionDetailResponse;
import com.be.back_end.dto.response.TransactionResponse;
import com.be.back_end.enums.BookingEnums;
import com.be.back_end.model.*;


import com.be.back_end.repository.AccountRepository;
import com.be.back_end.repository.BookingDetailsRepository;
import com.be.back_end.repository.BookingRepository;
import com.be.back_end.repository.TranscationRepository;
import com.be.back_end.utils.VNPayUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class TranscationService implements ITranscationService, IVNPayService {

    private final TranscationRepository transcationRepository;
    private final VNPayUtils vnPayUtils;
    private final AccountRepository accountRepository;
    private final BookingRepository bookingRepository;
    private final BookingDetailsRepository bookingDetailsRepository;
    @Autowired
    public TranscationService(TranscationRepository transcationRepository, VNPayUtils vnPayUtils, AccountRepository accountRepository, BookingRepository bookingRepository, BookingDetailsRepository bookingDetailsRepository) {
        this.transcationRepository = transcationRepository;
        this.vnPayUtils=vnPayUtils;
        this.accountRepository = accountRepository;
        this.bookingRepository = bookingRepository;
        this.bookingDetailsRepository = bookingDetailsRepository;
    }

    @Override
    public TransactionDTO create(TransactionDTO dto) {
        Transaction newTransaction = mapToEntity(dto);
        transcationRepository.save(newTransaction);
        return dto;
    }

    @Override
    public List<TransactionDTO> getAll() {
        List<Transaction> transactions = transcationRepository.findAll();
        List<TransactionDTO> list = new ArrayList<>();
        for (Transaction transaction : transactions) {
            list.add(mapToDTO(transaction));
            System.out.println(transaction.getId());
        }
        return list;
    }

    @Override
    public TransactionDTO getById(String id) {
        Transaction transaction = transcationRepository.findById(id).orElse(null);
        return mapToDTO(transaction);
    }

    public List<TransactionDTO> getAllForCustomer(String CustomerId) {


        //Get a Customer
        Account account = accountRepository.findById(CustomerId).orElse(null);

        if (account != null) {
            List<Bookings> bookingsList = bookingRepository.findAllByAccount(account);
            if (bookingsList.isEmpty()) {
                System.out.println("No bookings found for customer ID: " + CustomerId);
                return Collections.emptyList();
            }

            //Get Transcation for every booking available. Flatten it to return every single transcation

            List<Transaction> transactionList = bookingsList.stream()
                    .flatMap(booking->transcationRepository.findAllByBookings(booking).stream())
                    .toList();

            if (transactionList.isEmpty()) {
                System.out.println("Transcation list is empty");
                return null;
            }

            //Convert them into DTO
            List<TransactionDTO> transactionDTOList = transactionList.stream()
                    .map(this::mapToDTO)
                    .toList();


            if (transactionDTOList.isEmpty()) {
                System.out.println("TranscationDTO list is empty");
                return null;
            }

            return transactionDTOList;

        }
        System.out.println("Customer is empty");
        return null;

    }


    @Override
    public TransactionDetailResponse getTransactionDetail(String id) {
        Transaction transaction= transcationRepository.findById(id).orElse(null);
        if(transaction==null){
            System.out.println("Transaction not found");
            return null;
        }

        Bookings booking= bookingRepository.findByTransactions(transaction);

        if(booking==null){
            System.out.println("Booking not found");
            return null;
        }

        List<Bookingdetails> bookingdetails = bookingDetailsRepository.findByBooking(booking);

        if(bookingdetails==null){
            System.out.println("BookingDetail not found");
            return null;
        }

        TransactionDTO transactionDTO =mapToDTO(transaction);
        List<BookingDetailResponseDTO> bookingDetailResponseDTO=bookingdetails.stream()
                .map(this::BookingDetailMapToDTO)
                .toList();

        return TransactionDetailResponse.builder()
                .transaction(transactionDTO)
                .bookingDetail(bookingDetailResponseDTO)
                .build();


    }

    private BookingDetailResponseDTO BookingDetailMapToDTO(Bookingdetails bookingdetails) {
        Designs design = bookingdetails.getDesign();

        return BookingDetailResponseDTO.builder()
                .bookingDetailId(bookingdetails.getId())
                .bookingId(bookingdetails.getBooking().getId())
                .designId(design.getId())
                .designFile(design.getDesignFile())
                .description(bookingdetails.getDescription())
                .unitPrice(bookingdetails.getUnit_price())
                .build();
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




    public String processPaymentCallback(HttpServletRequest request) {
        String responseCode = request.getParameter("vnp_ResponseCode");
        String bookingcode = request.getParameter("vnp_TxnRef");
        String bankCode = request.getParameter("vnp_BankCode");
        String amount = request.getParameter("vnp_Amount");
        String reason = request.getParameter("vnp_OrderInfo");
        String transactionMethod = request.getParameter("vnp_CardType");

        String transactionStatus = responseCode.equals("00") ? "SUCCESS" : "FAILED";
        createTransaction(bookingcode, amount, bankCode, transactionStatus, reason, transactionMethod);

        if ("SUCCESS".equals(transactionStatus)) {
            updateBookingStatus(bookingcode);
        }

        return transactionStatus;
    }


    private void createTransaction(String bookingCode, String amount, String bankCode,
                                   String transactionStatus, String reason, String transactionMethod) {
        Bookings booking = bookingRepository.findByCode(bookingCode)
                .orElseThrow(() -> new RuntimeException("Booking not found with code: " + bookingCode));
        BigDecimal transactionAmount;
        try {
            transactionAmount = new BigDecimal(amount).divide(BigDecimal.valueOf(100));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid amount format: " + amount);
        }
        Transaction transaction = new Transaction();
        transaction.setBookings(booking);
        transaction.setTransactionName("VNPay Payment");
        transaction.setTransactionMethod(transactionMethod);
        transaction.setTransactionAmount(transactionAmount);
        transaction.setTransactionStatus(transactionStatus);
        transaction.setBankCode(bankCode);
        transaction.setReason(reason);
        transaction.setTransactionType("DEPOSITED");
        transaction.setTransactionDate(LocalDateTime.now());
        transcationRepository.save(transaction);
    }
    private void updateBookingStatus(String bookingCode) {
        Bookings booking = bookingRepository.findByCode(bookingCode)
                .orElseThrow(() -> new RuntimeException("Booking not found with code: " + bookingCode));
        booking.setStatus(BookingEnums.DEPOSIT_PAID);
        bookingRepository.save(booking);
    }


    public TransactionDTO mapToDTO(Transaction Transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(Transaction.getId());
        dto.setBookingId(Transaction.getBookings().getId());
        dto.setTransactionName(Transaction.getTransactionName());
        dto.setTransactionMethod(Transaction.getTransactionMethod());
        dto.setTransactionDate(Transaction.getTransactionDate());
        dto.setTransactionStatus(Transaction.getTransactionStatus());
        dto.setTransactionAmount(Transaction.getTransactionAmount());
        dto.setTransactionType(Transaction.getTransactionType());
        dto.setReason(Transaction.getReason());
        dto.setBankCode(Transaction.getBankCode());
        return dto;
    }

    //Be careful of this function
    public Transaction mapToEntity(TransactionDTO dto) {
        Transaction transaction = new Transaction();
        Bookings bookings= bookingRepository.findById(dto.getBookingId()).orElse(null);
        transaction.setId(dto.getId());
        transaction.setBookings(bookings);
        transaction.setTransactionName(dto.getTransactionName());
        transaction.setTransactionMethod(dto.getTransactionMethod());
        transaction.setTransactionDate(dto.getTransactionDate());
        transaction.setTransactionStatus(dto.getTransactionStatus());
        transaction.setTransactionAmount(dto.getTransactionAmount());
        transaction.setTransactionType(dto.getTransactionType());
        transaction.setReason(dto.getReason());
        transaction.setBankCode(dto.getBankCode());
        return transaction;
    }
}

