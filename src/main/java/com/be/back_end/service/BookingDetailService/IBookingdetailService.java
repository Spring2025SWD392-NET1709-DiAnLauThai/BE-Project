package com.be.back_end.service.BookingDetailService;

import com.be.back_end.dto.request.BookingCreateRequest;
import com.be.back_end.dto.request.UpdateBookingDetailsRequest;
import com.be.back_end.dto.response.BookingCreateResponse;
import com.be.back_end.dto.response.BookingResponseInDetail;
import com.be.back_end.dto.response.BookingResponseInDetailCus;
import com.be.back_end.model.Bookingdetails;
import com.be.back_end.model.Bookings;
import com.be.back_end.model.Designs;

import java.util.List;

public interface IBookingdetailService {
    Bookingdetails createBookingDetail(BookingCreateRequest.BookingDetailCreateRequest detailRequest, Bookings booking, Designs design);



    BookingResponseInDetail getAllBookingDetailsByBookingId(String bookingId);
    BookingResponseInDetailCus getAllBookingDetailsByBookingIdForCustomer(String bookingId);
    boolean updatebookingdetail(UpdateBookingDetailsRequest dto);

    List<BookingCreateResponse.BookingDetailResponse> processBookingDetails(BookingCreateRequest request, Bookings booking);

}
