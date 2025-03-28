package com.be.back_end.service.BookingService;

import com.be.back_end.dto.request.BookingCreateRequest;
import com.be.back_end.dto.request.CancelBookingRequest;
import com.be.back_end.dto.request.PublicTshirtRequest;
import com.be.back_end.dto.response.BookingCreateResponse;
import com.be.back_end.dto.response.BookingResponse;
import com.be.back_end.dto.response.PaginatedResponseDTO;
import com.be.back_end.enums.BookingEnums;
import com.be.back_end.enums.RoleEnums;
import com.be.back_end.enums.TaskStatusEnum;
import com.be.back_end.enums.TransactionStatusEnum;
import com.be.back_end.model.Bookingdetails;
import com.be.back_end.model.Bookings;
import com.be.back_end.model.Task;
import com.be.back_end.model.Transaction;
import com.be.back_end.repository.BookingDetailsRepository;
import com.be.back_end.repository.BookingRepository;
import com.be.back_end.repository.TaskRepository;
import com.be.back_end.repository.TransactionRepository;
import com.be.back_end.service.BookingDetailService.IBookingdetailService;
import com.be.back_end.service.CloudinaryService.ICloudinaryService;
import com.be.back_end.service.DesignService.IDesignService;
import com.be.back_end.service.EmailService.IEmailService;
import com.be.back_end.service.TranscationService.ITransactionService;
import com.be.back_end.service.TranscationService.IVNPayService;
import com.be.back_end.utils.AccountUtils;
import com.be.back_end.utils.VNPayUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookingService implements IBookingService {
    private final BookingRepository bookingRepository;
    private final AccountUtils accountUtils;
    private final IDesignService designService;
    private final IBookingdetailService bookingdetailService;
    private final BookingDetailsRepository bookingDetailsRepository;
    private final ICloudinaryService cloudinaryService;
    private final VNPayUtils  vnPayUtils;
    private final TaskRepository taskRepository;
    private final ITransactionService transcationService;
    private final TransactionRepository transactionRepository;
    private final IEmailService emailService;

    public BookingService(BookingRepository bookingRepository, AccountUtils accountUtils, IDesignService designService, IBookingdetailService bookingdetailService, BookingDetailsRepository bookingDetailsRepository, ICloudinaryService cloudinaryService, IVNPayService ivnPayService, VNPayUtils vnPayUtils, TaskRepository taskRepository, ITransactionService transcationService, TransactionRepository transactionRepository, IEmailService emailService) {
        this.bookingRepository = bookingRepository;
        this.accountUtils = accountUtils;
        this.designService = designService;
        this.bookingdetailService = bookingdetailService;
        this.bookingDetailsRepository = bookingDetailsRepository;
        this.cloudinaryService = cloudinaryService;
        this.vnPayUtils = vnPayUtils;
        this.taskRepository = taskRepository;
        this.transcationService = transcationService;
        this.transactionRepository = transactionRepository;
        this.emailService = emailService;
    }

    private String generateBookingCode(int length) {
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "0123456789";
        String allCharacters = upperCaseLetters + numbers;

        SecureRandom random = new SecureRandom();
        String bookingCode;

        do {
            StringBuilder sb = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                sb.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
            }
            bookingCode = sb.toString();
        } while (bookingRepository.existsByCode(bookingCode));

        return bookingCode;
    }




    @Transactional
    @Override
    public String generateFullyPaidUrl(String bookingId, HttpServletRequest request) {

        Bookings booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + bookingId));

        BigDecimal remainingBalance = booking.getTotal_price().subtract(booking.getDepositAmount());
        String paymentUrl;
        try {
            paymentUrl = vnPayUtils.generatePaymentUrlWithBookingId(
                    remainingBalance.toString(),
                    booking.getTitle(),
                    TransactionStatusEnum.FULLY_PAID.toString(),
                    request,
                    booking.getCode(),
                    TransactionStatusEnum.FULLY_PAID
            );
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error generating VNPay URL", e);
        }
        return paymentUrl;
    }
    @Override
    public String repayBooking(String bookingId, HttpServletRequest request) {
        Bookings booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + bookingId));
        String status = booking.getStatus().toString();
        String paymentUrl;
        try {
            if (status.equalsIgnoreCase(BookingEnums.UNPAID.toString())) {
                paymentUrl = vnPayUtils.generatePaymentUrlWithBookingId(
                        booking.getDepositAmount() != null
                                ? booking.getDepositAmount().toString()
                                : "0",
                        booking.getTitle(),
                        booking.getStatus().toString(),
                        request,
                        booking.getCode(),
                        TransactionStatusEnum.DEPOSITED
                );
            } else {
                throw new IllegalArgumentException("Invalid booking status: " + status);
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error generating VNPay URL", e);
        }

        return paymentUrl;
    }


    @Transactional
    @Override
    public boolean cancelBooking(CancelBookingRequest cancelBookingRequest) {
        Bookings bookings = bookingRepository.findById(cancelBookingRequest.getBookingId()).orElse(null);
        if (bookings == null) {
            return false;
        }
        Task task = taskRepository.findByBookingId(cancelBookingRequest.getBookingId()).orElse(null);
        if (task != null) {
            task.setTaskStatus(TaskStatusEnum.CANCEL.toString());
            taskRepository.save(task);
        }
        bookings.setStatus(BookingEnums.CANCELLED);
        bookings.setNote(cancelBookingRequest.getNote());
        Transaction transaction = transactionRepository.findByBooking_Id(cancelBookingRequest.getBookingId()).orElse(null);
        if (transaction == null) {
            return false;
        }
        transaction.setTransactionStatus(TransactionStatusEnum.REFUND.toString());
        bookingRepository.save(bookings);
        transactionRepository.save(transaction);
        return true;
    }




    private BigDecimal getTotalPriceByBookingId(String bookingId) {
        List<Bookingdetails> bookingDetails = bookingDetailsRepository.findByBookingId(bookingId);
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (Bookingdetails detail : bookingDetails) {
            if (detail.getUnit_price() != null) {
                totalPrice = totalPrice.add(detail.getUnit_price());
            }
        }

        return totalPrice;
    }

    private int getTotalQuantityByBookingId(String bookingId) {
        return bookingDetailsRepository.findByBookingId(bookingId).size();
    }
    @Override
    public BookingCreateResponse createBooking(BookingCreateRequest request, HttpServletRequest httpRequest) {

        Bookings booking = createAndSaveNewBooking(request);
        List<BookingCreateResponse.BookingDetailResponse> bookingDetails = new ArrayList<>();
        if (request.getBookingdetails() != null) {
            bookingDetails = bookingdetailService.processBookingDetails(request, booking);
        }

        BigDecimal totalPrice = getTotalPriceByBookingId(booking.getId());
        Integer totalQuantity = getTotalQuantityByBookingId(booking.getId());
        BigDecimal depositAmount = totalPrice != null
                ? totalPrice.multiply(BigDecimal.valueOf(0.5))
                : BigDecimal.ZERO;
        booking.setTotal_price(totalPrice != null ? totalPrice : BigDecimal.ZERO);
        booking.setDepositAmount(depositAmount);
        booking.setTotal_quantity(totalQuantity != null ? totalQuantity : 0);
        bookingRepository.save(booking);
        String paymentUrl;
        try {
            paymentUrl = vnPayUtils.generatePaymentUrlWithBookingId(
                    depositAmount.toString(),
                    booking.getTitle(),
                    booking.getStatus().toString(),
                    httpRequest,
                    booking.getCode(),
                    TransactionStatusEnum.DEPOSITED
            );

        } catch (UnsupportedEncodingException e) {
            paymentUrl = null;
        }
        return new BookingCreateResponse(
                booking.getId(),
                booking.getTotal_price(),
                booking.getTotal_quantity(),
                booking.getAccount().getId(),
                booking.getCode(),
                booking.getTitle(),
                booking.getStatus(),
                booking.getStartDate(),
                booking.getEndDate(),
                bookingDetails,
                paymentUrl
        );
    }











    @Override
    public Bookings createAndSaveNewBooking(BookingCreateRequest bookingDTO) {
        Bookings booking = new Bookings();
        booking.setStatus(BookingEnums.UNPAID);
        booking.setStartDate(bookingDTO.getStartdate());
        booking.setEndDate(bookingDTO.getEnddate());
        booking.setTitle(bookingDTO.getTitle());
        String bookingCode = generateBookingCode(8);
        booking.setAccount(accountUtils.getCurrentAccount());

        booking.setCode(bookingCode);
        return bookingRepository.save(booking);
    }


    @Transactional()
    @Override
    public PaginatedResponseDTO<BookingResponse> getAllBookings(
            int page,
            int size,
            String sortBy,
            String direction) {
        RoleEnums currentRole = accountUtils.getCurrentAccount().getRole();
        String sortField = (sortBy == null || sortBy.trim().isEmpty()) ? "dateCreated" : sortBy;
        Sort.Direction sortDirection = (direction == null || direction.trim().isEmpty()) ?
                Sort.Direction.ASC : Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sortDirection, sortField));
        Page<Bookings> bookingsPage = currentRole.equals(RoleEnums.CUSTOMER) ?
                bookingRepository.findAllByAccount(accountUtils.getCurrentAccount(), pageable) :
                bookingRepository.findAll(pageable);
        List<String> bookingIds = bookingsPage.getContent().stream()
                .map(Bookings::getId)
                .collect(Collectors.toList());
        List<Task> tasks = taskRepository.findByBookingIdIn(bookingIds);
        Map<String, String> designerMap = tasks.stream()
                .collect(Collectors.toMap(
                        task -> task.getBooking().getId(),
                        task -> task.getAccount().getName(),
                        (existing, replacement) -> existing));
        List<BookingResponse> bookingResponseList = bookingsPage.getContent().stream()
                .map(booking->
        {
            BookingResponse response = new BookingResponse();
            response.setId(booking.getId());
            response.setTitle(booking.getTitle());
            response.setStatus(booking.getStatus().toString());
            response.setEndDate(booking.getEndDate());
            response.setStartDate(booking.getStartDate());
            response.setUpdateDate(booking.getDateUpdated());
            response.setCreatedDate(booking.getDateCreated());
            response.setTotalQuantity(booking.getTotal_quantity());
            response.setDepositAmount(booking.getDepositAmount());
            response.setTotalPrice(booking.getTotal_price());
            response.setCode(booking.getCode());
            response.setAssignedDesigner(designerMap.get(booking.getId()));
            return response;
        }).collect(Collectors.toList());
        PaginatedResponseDTO<BookingResponse> response = new PaginatedResponseDTO<>();
        response.setContent(bookingResponseList);
        response.setPageSize(bookingsPage.getSize());
        response.setPageNumber(bookingsPage.getNumber());
        response.setTotalPages(bookingsPage.getTotalPages());
        response.setTotalElements(bookingsPage.getTotalElements());
        return response;
    }


    @Scheduled(cron = "0 0 0 * * ?")
    public void markCompletedBookings() {
        List<Bookings> bookings = bookingRepository.findByStatusNotAndEndDateBefore(
                BookingEnums.COMPLETED, LocalDateTime.now()
        );
        for(Bookings booking:bookings){
            processBooking(booking);
        }

    }
    @Transactional
    public void processBooking(Bookings booking)
    {
        Task task=taskRepository.findByBookingId(booking.getId()).orElse(null);
        if (task != null) {
            booking.setStatus(BookingEnums.COMPLETED);
            task.setTaskStatus(TaskStatusEnum.COMPLETE.toString());
            bookingRepository.save(booking);
            taskRepository.save(task);
            String customerEmail = booking.getAccount().getEmail();
            String customerName = booking.getAccount().getName();
            String bookingCode = booking.getCode();
            try {
                emailService.sendCustomerCompleteEmail(customerEmail, customerName, bookingCode);
            }catch (RuntimeException e){
                System.out.println("Failed to send email for booking ID: "
                        + booking.getId() + " - " + e.getMessage());
            }

        } else {
            System.out.println("Task not found for booking ID: " + booking.getId());
        }
    }

    @Transactional
    @Override
    public boolean publicTshirt(String bookingId) {
      Bookings bookings=bookingRepository.findById(bookingId).orElse(null);
        if (bookings == null) {
            throw new IllegalArgumentException("Booking not found with ID: " + bookingId);
        }
      bookings.setPublic(true);
      bookingRepository.save(bookings);
      return true;
    }
}
