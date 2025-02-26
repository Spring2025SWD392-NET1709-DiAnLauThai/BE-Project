package com.be.back_end.service.BookingService;

import com.be.back_end.dto.BookingDTO;
import com.be.back_end.model.Bookings;
import com.be.back_end.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService implements IBookingService {
    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public boolean createBooking(BookingDTO bookingDTO) {
        Bookings order = new Bookings();
        order.setTotal_price(bookingDTO.getTotal_price());
        order.setTotal_quantity(bookingDTO.getTotal_quantity());
        order.setStatus(bookingDTO.getStatus());
        order.setOrder_notes(bookingDTO.getOrder_notes());

        bookingRepository.save(order);
        return true;
    }

    @Override
    public List<Bookings> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Bookings getBookingById(String id) {
        return bookingRepository.findById(id).orElse(null);
    }

    @Override
    public Bookings updateBooking(String id, BookingDTO bookingDTO) {
        Bookings order = bookingRepository.findById(id).orElse(null);
        if (order != null) {
            order.setTotal_price(bookingDTO.getTotal_price());
            order.setTotal_quantity(bookingDTO.getTotal_quantity());
            order.setStatus(bookingDTO.getStatus());
            order.setOrder_notes(bookingDTO.getOrder_notes());
            return bookingRepository.save(order);
        }
        return null;
    }

    @Override
    public boolean deleteBooking(String id) {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
