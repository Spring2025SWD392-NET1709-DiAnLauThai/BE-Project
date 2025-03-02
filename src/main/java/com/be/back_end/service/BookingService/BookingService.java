package com.be.back_end.service.BookingService;

import com.be.back_end.dto.BookingDTO;
import com.be.back_end.enums.BookingEnums;
import com.be.back_end.model.Bookings;
import com.be.back_end.repository.BookingRepository;
import com.be.back_end.utils.AccountUtils;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService implements IBookingService {
    private final BookingRepository bookingRepository;
    private final AccountUtils accountUtils;

    public BookingService(BookingRepository bookingRepository, AccountUtils accountUtils) {
        this.bookingRepository = bookingRepository;
        this.accountUtils = accountUtils;
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
    public boolean createbooking(BookingDTO bookingDTO) {

        Bookings booking = new Bookings();
        booking.setStatus(BookingEnums.PENDING);
        booking.setTotal_price(bookingDTO.getTotal_price());
        booking.setDate_created(LocalDateTime.now());
        booking.setLast_updated(LocalDateTime.now());
        booking.setTitle(bookingDTO.getTitle());
        booking.setDuration(bookingDTO.getDuration());
        String bookingCode = generateBookingCode(8);
        booking.setAccount(accountUtils.getCurrentAccount());
        booking.setFeedback(null);
        booking.setCode(bookingCode);

        bookingRepository.save(booking);
        return true;
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
