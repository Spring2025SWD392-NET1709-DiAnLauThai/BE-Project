package com.be.back_end.service.BookingService;

import com.be.back_end.dto.request.BookingCreateRequest;
import com.be.back_end.dto.request.CancelBookingRequest;
import com.be.back_end.dto.request.PublicTshirtRequest;
import com.be.back_end.dto.response.BookingCreateResponse;
import com.be.back_end.dto.response.BookingResponse;
import com.be.back_end.dto.response.PaginatedResponseDTO;
import com.be.back_end.model.Bookings;
import jakarta.servlet.http.HttpServletRequest;

public interface IBookingService {
    Bookings createAndSaveNewBooking(BookingCreateRequest bookingDTO);
    public PaginatedResponseDTO<BookingResponse> getAllBookings(
            int page,
            int size);

    BookingCreateResponse createBooking(BookingCreateRequest request, HttpServletRequest httpRequest);
    boolean publicTshirt(String bookingId);
    String generateFullyPaidUrl(String bookingId, HttpServletRequest request);
    boolean cancelBooking(CancelBookingRequest cancelBookingRequest);
    String repayBooking(String bookingId, HttpServletRequest request);
}