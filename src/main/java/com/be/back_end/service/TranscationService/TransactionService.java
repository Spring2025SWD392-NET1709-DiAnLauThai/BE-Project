
package com.be.back_end.service.TranscationService;

import com.be.back_end.dto.response.*;


import com.be.back_end.enums.*;
import com.be.back_end.model.*;


import com.be.back_end.repository.AccountRepository;
import com.be.back_end.repository.BookingDetailsRepository;
import com.be.back_end.repository.BookingRepository;
import com.be.back_end.repository.TransactionRepository;
import com.be.back_end.utils.AccountUtils;
import com.be.back_end.utils.VNPayUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransactionService implements ITransactionService, IVNPayService {

    private final TransactionRepository transactionRepository;
    private final VNPayUtils vnPayUtils;
    private final AccountRepository accountRepository;
    private final BookingRepository bookingRepository;
    private final BookingDetailsRepository bookingDetailsRepository;

    private final AccountUtils accountUtils;
    @Autowired
    public TransactionService(TransactionRepository transactionRepository, VNPayUtils vnPayUtils, AccountRepository accountRepository, BookingRepository bookingRepository, BookingDetailsRepository bookingDetailsRepository,
                              AccountUtils accountUtils) {
        this.transactionRepository = transactionRepository;
        this.vnPayUtils=vnPayUtils;
        this.accountRepository = accountRepository;
        this.bookingRepository = bookingRepository;
        this.bookingDetailsRepository = bookingDetailsRepository;
        this.accountUtils = accountUtils;
    }

    @Override
    public TransactionDTO create(TransactionDTO dto) {
        Transaction newTransaction = mapToEntity(dto);
        transactionRepository.save(newTransaction);
        return dto;
    }



    public PaginatedResponseDTO<TransactionDTO> getAll(int page, int size, String sortDir, String sortBy) {
        Sort.Direction sort = sortDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page-1, size,sort,sortBy);
        Page<Transaction> transactions;

        transactions = transactionRepository.findAll(pageable);

        List<TransactionDTO> transactionDTO = new ArrayList<>();
        for (Transaction transaction : transactions.getContent()) {
            transactionDTO.add(mapToDTO(transaction));
        }


        PaginatedResponseDTO<TransactionDTO> response = new PaginatedResponseDTO<>();
        response.setContent(transactionDTO);
        response.setPageNumber(transactions.getNumber());
        response.setPageSize(transactions.getSize());
        response.setTotalElements(transactions.getTotalElements());
        response.setTotalPages(transactions.getTotalPages());

        return response;
    }

    @Override
    public TransactionDTO getById(String id) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        return mapToDTO(transaction);
    }



    public Page<Transaction> getTransactionPageByCustomer(Pageable pageable) {

        //Get a Customer
        Account currentAccount = accountUtils.getCurrentAccount();
        String customerId = currentAccount.getId();
        Account account = accountRepository.findById(customerId).orElse(null);

        if (account != null) {

            //Get Transaction for every booking available from the Booking List
            Page<Transaction> transactionPage = transactionRepository.findAllByAccountId(account.getId(),pageable);

            if (transactionPage.isEmpty()) {
                System.out.println("Transaction page is empty");
                return null;
            }
            return transactionPage;
        }
        System.out.println("Customer is empty");
        return null;

    }

    public PaginatedResponseDTO<TransactionDTO> getAllByCustomer(int page, int size, String sortDir, String sortBy) {


        Sort.Direction sort = sortDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page-1, size,sort,sortBy);
        Page<Transaction> transactions;

        transactions = getTransactionPageByCustomer(pageable);

        List<TransactionDTO> transactionDTO = new ArrayList<>();
        for (Transaction transaction : transactions.getContent()) {
            transactionDTO.add(mapToDTO(transaction));
        }

        if(transactionDTO.isEmpty()){
            System.out.println("TransactionDTO is empty or can't be created");
            return null;
        }

        PaginatedResponseDTO<TransactionDTO> response = new PaginatedResponseDTO<>();
        response.setContent(transactionDTO);
        response.setPageNumber(transactions.getNumber());
        response.setPageSize(transactions.getSize());
        response.setTotalElements(transactions.getTotalElements());
        response.setTotalPages(transactions.getTotalPages());

        return response;
    }



    @Override
    public TransactionDetailResponse getTransactionDetail(String id) {
        Transaction transaction= transactionRepository.findById(id).orElse(null);
        if(transaction==null){
            System.out.println("Transaction not found");
            return null;
        }

        Bookings booking= bookingRepository.findByTransaction(transaction);

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
    @Override
    public String processDepositCallback(HttpServletRequest request) {

        String responseCode = request.getParameter("vnp_ResponseCode");
        String bookingcode = vnPayUtils.decodeBookingCode(request.getParameter("vnp_TxnRef"));
        String bankCode = request.getParameter("vnp_BankCode");
        String amount = request.getParameter("vnp_Amount");
        String transactionMethod = request.getParameter("vnp_CardType");

        String vnpayStatus = responseCode.equals("00") ? "SUCCESS" :"FAILED";

        if ("SUCCESS".equals(vnpayStatus)) {
            updateBookingStatus(bookingcode);
            createTransaction(bookingcode, amount, bankCode, TransactionStatusEnum.DEPOSITED.toString(), transactionMethod);
        }


        return vnpayStatus;
    }



    @Override
    public String processPaymentCallback(HttpServletRequest request) {

        String responseCode = request.getParameter("vnp_ResponseCode");
        String bookingCode = vnPayUtils.decodeBookingCode(request.getParameter("vnp_TxnRef"));


        String vnpayStatus = responseCode.equals("00") ? "SUCCESS" :"FAILED";

        if ("SUCCESS".equals(vnpayStatus)) {
            Bookings booking = bookingRepository.findByCode(bookingCode)
                    .orElseThrow(() -> new RuntimeException("Booking not found with code: " + bookingCode));
            Transaction transaction= transactionRepository.findByBooking_Id(booking.getId())
                    .orElseThrow(() -> new RuntimeException("Transaction not found"));
            transaction.setTransactionStatus(TransactionStatusEnum.FULLY_PAID.toString());
            transactionRepository.save(transaction);

        }
         return vnpayStatus;
    }


    private Transaction createTransaction(String bookingCode, String amount, String bankCode,
                                   String transactionStatus, String transactionMethod) {
        Bookings booking = bookingRepository.findByCode(bookingCode)
                .orElseThrow(() -> new RuntimeException("Booking not found with code: " + bookingCode));
        BigDecimal transactionAmount;
        try {
            transactionAmount = booking.getDepositAmount();
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid amount format: " + amount);
        }
        Transaction transaction = new Transaction();
        transaction.setBooking(booking);
        transaction.setTransactionName("VNPay Payment");
        transaction.setTransactionMethod(transactionMethod);
        transaction.setTransactionAmount(transactionAmount);
        transaction.setTransactionStatus(transactionStatus);
        transaction.setBankCode(bankCode);
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);
        return transaction;
    }

    private void updateBookingStatus(String bookingCode) {
        Bookings booking = bookingRepository.findByCode(bookingCode)
                .orElseThrow(() -> new RuntimeException("Booking not found with code: " + bookingCode));
        booking.setStatus(BookingEnums.DEPOSITED);
        bookingRepository.save(booking);
    }


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


    public Transaction mapToEntity(TransactionDTO dto) {
        Transaction transaction = new Transaction();
        Bookings bookings= bookingRepository.findById(dto.getBookingId()).orElse(null);
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


    //Calculate total income of all transaction given date
    //Date format is 2025-03-18 (YYYY-MM-DD) ISO format
    @Override
    public BigDecimal calculateTotalIncomeByDate(LocalDate date) {

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);


        List<Transaction> fullyPaidTransactions = transactionRepository.findFullyPaidTransactionsByDateRange(startOfDay, endOfDay);


        return fullyPaidTransactions.stream()
                .map(Transaction::getTransactionAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public MonthlyIncomeResponse calculateMonthlyIncome(int year, Month month) {
        // Get the first and last day of the month
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

        // Convert to LocalDateTime for database query
        LocalDateTime startDateTime = firstDayOfMonth.atStartOfDay();
        LocalDateTime endDateTime = lastDayOfMonth.plusDays(1).atStartOfDay();

        // Get all fully paid transactions within the month
        List<Transaction> fullyPaidTransactions = transactionRepository
                .findFullyPaidTransactionsByDateRange(startDateTime, endDateTime);

        // Calculate total income for the month
        BigDecimal totalIncome = fullyPaidTransactions.stream()
                .map(Transaction::getTransactionAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Group transactions by date and calculate daily totals
        Map<LocalDate, BigDecimal> dailyTotalsMap = fullyPaidTransactions.stream()
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getTransactionDate().toLocalDate(),
                        Collectors.mapping(
                                Transaction::getTransactionAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));

        // Create a list with all days of the month (including those with zero income)
        List<DailyIncomeResponse> dailyTransactions = new ArrayList<>();

        // Initialize all days with zero
        for (int day = 1; day <= lastDayOfMonth.getDayOfMonth(); day++) {
            LocalDate currentDate = LocalDate.of(year, month, day);
            BigDecimal dailyAmount = dailyTotalsMap.getOrDefault(currentDate, BigDecimal.ZERO);
            dailyTransactions.add(new DailyIncomeResponse (currentDate, dailyAmount));
        }

        // Create and return the response
        return new MonthlyIncomeResponse(month, year, totalIncome, dailyTransactions);
    }

    @Override
    public YearlyIncomeResponse calculateYearlyIncome(int year) {
        // Calculate start and end of the year
        LocalDateTime startOfYear = LocalDate.of(year, 1, 1).atStartOfDay();
        LocalDateTime endOfYear = LocalDate.of(year, 12, 31).plusDays(1).atStartOfDay();

        // Get all fully paid transactions within the year
        List<Transaction> fullyPaidTransactions = transactionRepository
                .findFullyPaidTransactionsByDateRange(startOfYear, endOfYear);

        // Calculate total income for the year
        BigDecimal totalYearlyIncome = fullyPaidTransactions.stream()
                .map(Transaction::getTransactionAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Group transactions by month and calculate monthly totals
        Map<Month, BigDecimal> monthlyTotals = fullyPaidTransactions.stream()
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getTransactionDate().getMonth(),
                        Collectors.mapping(
                                Transaction::getTransactionAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));

        // Create a list of monthly income summaries for all months (including those with zero income)
        List<MonthlyIncomeSummary> monthlyIncomes = new ArrayList<>();
        for (Month month : Month.values()) {
            BigDecimal monthlyIncome = monthlyTotals.getOrDefault(month, BigDecimal.ZERO);
            monthlyIncomes.add(new MonthlyIncomeSummary(month, monthlyIncome));
        }

        // Sort by month
        monthlyIncomes.sort(Comparator.comparing(MonthlyIncomeSummary::getMonth));

        // Create and return the response
        return new YearlyIncomeResponse(year, totalYearlyIncome, monthlyIncomes);
    }
}

