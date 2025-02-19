package com.be.back_end.service.BookingDetailService;

import com.be.back_end.dto.BookingdetailsDTO;

import com.be.back_end.model.Bookingdetails;

import java.util.List;

public interface IBookingdetailService {
    boolean createbookingdetail(BookingdetailsDTO dto);
    List<Bookingdetails> getAllbookingdetails();
    Bookingdetails getbookingdetailById(String id);
    Bookingdetails updatebookingdetail(String id, BookingdetailsDTO dto);
    boolean deletebookingdetail(String id);
}
