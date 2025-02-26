package com.be.back_end.service.BookingItemService;

import com.be.back_end.dto.BookingitemsDTO;
import com.be.back_end.model.Bookingitems;

import java.util.List;

public interface IBookingitemsService {
    boolean createBookingItem(BookingitemsDTO dto);
    List<Bookingitems> getAllBookingItems();
    Bookingitems getBookingItemById(String id);
    Bookingitems updateBookingItem(String id, BookingitemsDTO dto);
    boolean deleteBookingItem(String id);
}
