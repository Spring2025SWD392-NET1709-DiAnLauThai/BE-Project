package com.be.back_end.controller;

import com.be.back_end.dto.BookingdetailsDTO;

import com.be.back_end.dto.response.ApiResponse;
import com.be.back_end.dto.response.BookingDetailResponseDTO;
import com.be.back_end.dto.response.ErrorResponse;
import com.be.back_end.dto.response.PaginatedResponseDTO;
import com.be.back_end.model.Bookingdetails;
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
            @PathVariable String bookingId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (page < 1 || size <= 0) {
            ErrorResponse errorResponse = new ErrorResponse(400, "Invalid page or size", List.of("Page and size must be positive values"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        PaginatedResponseDTO<BookingDetailResponseDTO> bookingDetails = bookingdetailService.getAllBookingDetailsByBookingId(bookingId, page, size);
        if (bookingDetails.getContent().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        ApiResponse<PaginatedResponseDTO<BookingDetailResponseDTO>> apiResponse = new ApiResponse<>(
                200,
                bookingDetails,
                "Booking details retrieved successfully"
        );
        return ResponseEntity.ok(apiResponse);
    }
}