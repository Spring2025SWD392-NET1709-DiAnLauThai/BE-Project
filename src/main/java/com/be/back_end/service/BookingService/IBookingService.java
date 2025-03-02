package com.be.back_end.service.BookingService;

import com.be.back_end.dto.BookingDTO;
import com.be.back_end.model.Bookings;

import java.util.List;

public interface IBookingService {
    boolean createbooking(BookingDTO bookingDTO);
    List<Bookings> getAllbookings();
    Bookings getbookingById(String id);

    boolean deletebooking(String id);
}