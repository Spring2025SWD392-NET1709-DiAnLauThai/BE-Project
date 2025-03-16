package com.be.back_end.controller;

import com.be.back_end.dto.request.UpdateBookingDetailsRequest;
import com.be.back_end.dto.response.ApiResponse;
import com.be.back_end.dto.response.BookingResponseInDetail;
import com.be.back_end.dto.response.ErrorResponse;
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
    public ResponseEntity<?> getBookingDetailsByBookingId(@PathVariable String bookingId) {
        BookingResponseInDetail bookingDetails = bookingdetailService.getAllBookingDetailsByBookingId(bookingId);
        if (bookingDetails == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ErrorResponse(
                            404,
                            "Booking details not found",
                            List.of("Invalid booking ID or no details available")
                    )
            );
        }
        ApiResponse<BookingResponseInDetail> apiResponse = new ApiResponse<>(
                200,
                bookingDetails,
                "Booking details retrieved successfully"
        );
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/bookings/{bookingId}/details/customer")
    public ResponseEntity<?> getBookingDetailsByBookingIdForCustoemr(@PathVariable String bookingId) {
        BookingResponseInDetail bookingDetails = bookingdetailService.getAllBookingDetailsByBookingIdForCustomer(bookingId);
        if (bookingDetails == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ErrorResponse(
                            404,
                            "Booking details not found",
                            List.of("Invalid booking ID or no details available")
                    )
            );
        }
        ApiResponse<BookingResponseInDetail> apiResponse = new ApiResponse<>(
                200,
                bookingDetails,
                "Booking details retrieved successfully"
        );
        return ResponseEntity.ok(apiResponse);
    }
    @PutMapping()
    public ResponseEntity<?> updateBookingDetail(@RequestBody UpdateBookingDetailsRequest dto) {
        boolean isUpdated = bookingdetailService.updatebookingdetail(dto);
        if (!isUpdated) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse(400, "Failed to update booking detail",
                            List.of("Email delivery failed or invalid booking detail."))
            );
        }
        return ResponseEntity.ok(new ApiResponse<>(200, null, "Booking detail updated successfully."));
    }

}