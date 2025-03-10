package com.be.back_end.controller;

import com.be.back_end.dto.response.ApiResponse;
import com.be.back_end.dto.response.BookingResponseNoLinkDTO;
import com.be.back_end.dto.response.ErrorResponse;
import com.be.back_end.dto.response.PaginatedResponseDTO;
import com.be.back_end.service.BookingDetailService.IBookingdetailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookingsdetails")
public class BookingDetailsController {
    private final IBookingdetailService bookingdetailService;

    public BookingDetailsController(IBookingdetailService bookingdetailService) {
        this.bookingdetailService = bookingdetailService;
    }

    @GetMapping("/bookings/{bookingId}/details")
    public ResponseEntity<?> getBookingDetailsByBookingId(
            @PathVariable String bookingId) {

        BookingResponseNoLinkDTO bookingDetails = bookingdetailService.getAllBookingDetailsByBookingId(bookingId);

        ApiResponse<BookingResponseNoLinkDTO> apiResponse = new ApiResponse<>(
                200,
                bookingDetails,
                "Booking details retrieved successfully"
        );
        return ResponseEntity.ok(apiResponse);
    }
}