package com.be.back_end.service.BookingService;

import com.be.back_end.dto.BookingDTO;
import com.be.back_end.dto.request.BookingCreateRequest;
import com.be.back_end.dto.response.BookingCreateResponse;
import com.be.back_end.dto.response.BookingResponse;
import com.be.back_end.dto.response.PaginatedResponseDTO;
import com.be.back_end.model.Bookings;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IBookingService {
    Bookings createAndSaveNewBooking(BookingCreateRequest bookingDTO);
    public PaginatedResponseDTO<BookingResponse> getAllBookings(
            int page,
            int size);
    Bookings getbookingById(String id);
    BookingCreateResponse createBooking(BookingCreateRequest request, HttpServletRequest httpRequest);
    boolean deletebooking(String id);
}