package com.be.back_end.service.BookingDetailService;

import com.be.back_end.dto.BookingdetailsDTO;

import com.be.back_end.dto.request.BookingCreateRequest;
import com.be.back_end.dto.response.BookingCreateResponse;
import com.be.back_end.model.Bookingdetails;
import com.be.back_end.model.Bookings;
import com.be.back_end.model.Designs;
import com.be.back_end.model.Tshirts;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IBookingdetailService {
    Bookingdetails createBookingDetail(BookingCreateRequest.BookingDetailCreateRequest detailRequest, Bookings booking, Designs design);



    List<Bookingdetails> getAllbookingdetails();
    Bookingdetails getbookingdetailById(String id);
    /*Bookingdetails updatebookingdetail(String id, BookingdetailsDTO dto);*/
    boolean deletebookingdetail(String id);
    List<BookingCreateResponse.BookingDetailResponse> processBookingDetails(BookingCreateRequest request, Bookings booking);

}
