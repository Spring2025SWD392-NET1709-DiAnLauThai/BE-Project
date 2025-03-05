package com.be.back_end.service.BookingService;

import com.be.back_end.dto.BookingDTO;
import com.be.back_end.dto.request.BookingCreateRequest;
import com.be.back_end.dto.response.BookingCreateResponse;
import com.be.back_end.model.Bookings;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IBookingService {
    Bookings createAndSaveNewBooking(BookingCreateRequest bookingDTO);
    List<Bookings> getAllbookings();
    Bookings getbookingById(String id);
    BookingCreateResponse createBooking(BookingCreateRequest request);
    boolean deletebooking(String id);
}