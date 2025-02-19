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
    public boolean createbooking(BookingDTO bookingDTO) {
        Bookings booking = new Bookings();
        booking.setTotal_price(bookingDTO.getTotal_price());
        booking.setTotal_quantity(bookingDTO.getTotal_quantity());
        booking.setStatus(bookingDTO.getStatus());


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
    public Bookings updatebooking(String id, BookingDTO bookingDTO) {
        Bookings booking = bookingRepository.findById(id).orElse(null);
        if (booking != null) {
            booking.setTotal_price(bookingDTO.getTotal_price());
            booking.setTotal_quantity(bookingDTO.getTotal_quantity());
            booking.setStatus(bookingDTO.getStatus());

            return bookingRepository.save(booking);
        }
        return null;
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
