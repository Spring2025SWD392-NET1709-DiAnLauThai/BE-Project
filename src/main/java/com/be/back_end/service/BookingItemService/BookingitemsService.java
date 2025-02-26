package com.be.back_end.service.BookingItemService;

import com.be.back_end.dto.BookingitemsDTO;
import com.be.back_end.model.Bookingitems;
import com.be.back_end.repository.BookingitemsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingitemsService implements IBookingitemsService {
    private final BookingitemsRepository bookingitemsRepository;

    public BookingitemsService(BookingitemsRepository bookingitemsRepository) {
        this.bookingitemsRepository = bookingitemsRepository;
    }

    @Override
    public boolean createBookingItem(BookingitemsDTO dto) {
        Bookingitems orderItem = new Bookingitems();
        orderItem.setQuantity(dto.getQuantity());
        orderItem.setUnit_price(dto.getUnit_price());

        return  bookingitemsRepository.save(orderItem)!=null;
    }

    @Override
    public List<Bookingitems> getAllBookingItems() {
        return bookingitemsRepository.findAll();
    }

    @Override
    public Bookingitems getBookingItemById(String id) {
        return bookingitemsRepository.findById(id).orElse(null);
    }

    @Override
    public Bookingitems updateBookingItem(String id, BookingitemsDTO dto) {
        Bookingitems orderItem = bookingitemsRepository.findById(id).orElse(null);
        if (orderItem != null) {
            orderItem.setQuantity(dto.getQuantity());
            orderItem.setUnit_price(dto.getUnit_price());
            return bookingitemsRepository.save(orderItem);
        }
        return null;
    }

    @Override
    public boolean deleteBookingItem(String id) {
        if (bookingitemsRepository.existsById(id)) {
            bookingitemsRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
