package com.be.back_end.service.BookingService;

import com.be.back_end.dto.request.BookingCreateRequest;
import com.be.back_end.dto.response.BookingCreateResponse;
import com.be.back_end.dto.response.BookingResponse;
import com.be.back_end.dto.response.PaginatedResponseDTO;
import com.be.back_end.enums.BookingEnums;
import com.be.back_end.enums.RoleEnums;
import com.be.back_end.model.Bookings;
import com.be.back_end.repository.BookingDetailsRepository;
import com.be.back_end.repository.BookingRepository;
import com.be.back_end.repository.TaskRepository;
import com.be.back_end.service.BookingDetailService.IBookingdetailService;
import com.be.back_end.service.CloudinaryService.ICloudinaryService;
import com.be.back_end.service.DesignService.IDesignService;
import com.be.back_end.service.TranscationService.IVNPayService;
import com.be.back_end.utils.AccountUtils;
import com.be.back_end.utils.VNPayUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

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
    public BookingService(BookingRepository bookingRepository, AccountUtils accountUtils, IDesignService designService, IBookingdetailService bookingdetailService, BookingDetailsRepository bookingDetailsRepository, ICloudinaryService cloudinaryService, IVNPayService ivnPayService, VNPayUtils vnPayUtils, TaskRepository taskRepository) {
        this.bookingRepository = bookingRepository;
        this.accountUtils = accountUtils;
        this.designService = designService;
        this.bookingdetailService = bookingdetailService;
        this.bookingDetailsRepository = bookingDetailsRepository;
        this.cloudinaryService = cloudinaryService;
        this.vnPayUtils = vnPayUtils;
        this.taskRepository = taskRepository;
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






    @Override
    public BookingCreateResponse createBooking(BookingCreateRequest request, HttpServletRequest httpRequest) {

        Bookings booking = createAndSaveNewBooking(request);
        List<BookingCreateResponse.BookingDetailResponse> bookingDetails = new ArrayList<>();
        if (request.getBookingdetails() != null) {
            bookingDetails = bookingdetailService.processBookingDetails(request, booking);
        }
        BigDecimal totalPrice = bookingRepository.getTotalPriceByBookingId(booking.getId());
        Integer totalQuantity = bookingRepository.getTotalQuantityByBookingId(booking.getId());
        booking.setTotal_price(totalPrice != null ? totalPrice : BigDecimal.ZERO);
        booking.setTotal_quantity(totalQuantity != null ? totalQuantity : 0);
        bookingRepository.save(booking);
        String paymentUrl;
        try {
            paymentUrl = vnPayUtils.generatePaymentUrlWithBookingId(
                    totalPrice.toString(),
                    booking.getTitle(),
                    booking.getStatus().toString(),
                    httpRequest,
                    booking.getCode()
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
                booking.getStartdate(),
                booking.getEnddate(),
                bookingDetails,
                paymentUrl
        );
    }








    @Override
    public Bookings createAndSaveNewBooking(BookingCreateRequest bookingDTO) {
        Bookings booking = new Bookings();
        booking.setStatus(BookingEnums.UNPAID);
        booking.setStartdate(bookingDTO.getStartdate());
        booking.setEnddate(bookingDTO.getEnddate());
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
            int size) {
        RoleEnums currentRole= accountUtils.getCurrentAccount().getRole();

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Bookings> bookingsPage;
        if(currentRole.equals(RoleEnums.CUSTOMER)){
            bookingsPage=bookingRepository.findAllByAccount(accountUtils.getCurrentAccount(),pageable);

        }else{
        bookingsPage = bookingRepository.findAll(pageable);}
        List<BookingResponse> bookingResponseList = new ArrayList<>();
        for (Bookings booking : bookingsPage.getContent()) {
            String designerName = taskRepository.findByBookingId(booking.getId())
                    .map(task -> task.getAccount().getName())
                    .orElse(null);

            BookingResponse response = new BookingResponse();
            response.setId(booking.getId());
            response.setTitle(booking.getTitle());
            response.setStatus(booking.getStatus().toString());
            response.setEndDate(booking.getEnddate());
            response.setStartDate(booking.getStartdate());
            response.setUpdateDate(booking.getUpdateddate());
            response.setCreatedDate(booking.getDatecreated());
            response.setTotalQuantity(booking.getTotal_quantity());
            response.setTotalPrice(booking.getTotal_price());
            response.setCode(booking.getCode());
            response.setAssignedDesigner(designerName);
            bookingResponseList.add(response);
        }
        PaginatedResponseDTO<BookingResponse> response = new PaginatedResponseDTO<>();
        response.setContent(bookingResponseList);
        response.setPageSize(bookingsPage.getSize());
        response.setPageNumber(bookingsPage.getNumber());
        response.setTotalPages(bookingsPage.getTotalPages());
        response.setTotalElements(bookingsPage.getTotalElements());

        return response;
    }




    @Override
    public Bookings getbookingById(String id) {
        return bookingRepository.findById(id).orElse(null);
    }



    @Override
    public boolean deletebooking(String id) {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
