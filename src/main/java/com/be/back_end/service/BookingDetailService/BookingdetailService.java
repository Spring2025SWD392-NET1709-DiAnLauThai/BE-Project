package com.be.back_end.service.BookingDetailService;

import com.be.back_end.dto.BookingdetailsDTO;

import com.be.back_end.model.Bookingdetails;
import com.be.back_end.repository.BookingDetailsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingdetailService implements IBookingdetailService {
    private final BookingDetailsRepository bookingDetailsRepository;

    public BookingdetailService(BookingDetailsRepository bookingDetailsRepository) {
        this.bookingDetailsRepository = bookingDetailsRepository;
    }

    @Override
    public boolean createbookingdetail(BookingdetailsDTO dto) {
        Bookingdetails bookingdetail = new Bookingdetails();
        bookingdetail.setQuantity(dto.getQuantity());
        bookingdetail.setUnit_price(dto.getUnit_price());

        return  bookingDetailsRepository.save(bookingdetail)!=null;
    }

    @Override
    public List<Bookingdetails> getAllbookingdetails() {
        return bookingDetailsRepository.findAll();
    }

    @Override
    public Bookingdetails getbookingdetailById(String id) {
        return bookingDetailsRepository.findById(id).orElse(null);
    }

    @Override
    public Bookingdetails updatebookingdetail(String id, BookingdetailsDTO dto) {
        Bookingdetails bookingdetail = bookingDetailsRepository.findById(id).orElse(null);
        if (bookingdetail != null) {
            bookingdetail.setQuantity(dto.getQuantity());
            bookingdetail.setUnit_price(dto.getUnit_price());
            return bookingDetailsRepository.save(bookingdetail);
        }
        return null;
    }

    @Override
    public boolean deletebookingdetail(String id) {
        if (bookingDetailsRepository.existsById(id)) {
            bookingDetailsRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
