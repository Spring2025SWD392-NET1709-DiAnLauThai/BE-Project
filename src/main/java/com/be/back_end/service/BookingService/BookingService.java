package com.be.back_end.service.BookingService;

import com.be.back_end.dto.BookingDTO;
import com.be.back_end.dto.request.BookingCreateRequest;
import com.be.back_end.dto.response.BookingCreateResponse;
import com.be.back_end.enums.BookingEnums;
import com.be.back_end.model.Account;
import com.be.back_end.model.Bookings;
import com.be.back_end.model.Designs;
import com.be.back_end.repository.BookingDetailsRepository;
import com.be.back_end.repository.BookingRepository;
import com.be.back_end.service.BookingDetailService.IBookingdetailService;
import com.be.back_end.service.CloudinaryService.ICloudinaryService;
import com.be.back_end.service.DesignService.IDesignService;
import com.be.back_end.utils.AccountUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService implements IBookingService {
    private final BookingRepository bookingRepository;
    private final AccountUtils accountUtils;
    private final IDesignService designService;
    private final IBookingdetailService bookingdetailService;
    private final BookingDetailsRepository bookingDetailsRepository;
    private final ICloudinaryService cloudinaryService;
    public BookingService(BookingRepository bookingRepository, AccountUtils accountUtils, IDesignService designService, IBookingdetailService bookingdetailService, BookingDetailsRepository bookingDetailsRepository, ICloudinaryService cloudinaryService) {
        this.bookingRepository = bookingRepository;
        this.accountUtils = accountUtils;
        this.designService = designService;
        this.bookingdetailService = bookingdetailService;
        this.bookingDetailsRepository = bookingDetailsRepository;
        this.cloudinaryService = cloudinaryService;
    }

    private String generateBookingCode(int length) {
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "0123456789";
        String allCharacters = upperCaseLetters + numbers;

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }

        return sb.toString();
    }





    @Override
    public BookingCreateResponse createBooking(BookingCreateRequest request) {
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

        return new BookingCreateResponse(
                booking.getId(),
                booking.getTotal_price(),
                booking.getTotal_quantity(),
                booking.getAccount().getId(),
                booking.getCode(),
                booking.getTitle(),
                booking.getDuration(),
                booking.getStatus(),
                booking.getDate_created(),
                booking.getLast_updated(),
                bookingDetails
        );
    }








    @Override
    public Bookings createAndSaveNewBooking(BookingCreateRequest bookingDTO) {
        Bookings booking = new Bookings();
        booking.setStatus(BookingEnums.DEPOSIT_PAID);
        booking.setDate_created(LocalDateTime.now());
        booking.setLast_updated(LocalDateTime.now());
        booking.setTitle(bookingDTO.getTitle());
        booking.setDuration(bookingDTO.getDuration());
        String bookingCode = generateBookingCode(8);
        booking.setAccount(accountUtils.getCurrentAccount());
        booking.setFeedback(null);
        booking.setCode(bookingCode);
        return bookingRepository.save(booking);
    }


    @Override
    public List<Bookings> getAllbookings() {
        return bookingRepository.findAll();
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
