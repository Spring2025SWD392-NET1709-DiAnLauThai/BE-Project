package com.be.back_end.service.BookingService;

import com.be.back_end.dto.BookingDTO;
import com.be.back_end.model.Bookings;

import java.util.List;

public interface IBookingService {
    boolean createBooking(BookingDTO bookingDTO);
    List<Bookings> getAllBookings();
    Bookings getBookingById(String id);
    Bookings updateBooking(String id, BookingDTO bookingDTO);
    boolean deleteBooking(String id);
}