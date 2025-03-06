package com.be.back_end.controller;

import com.be.back_end.dto.request.BookingCreateRequest;
import com.be.back_end.dto.response.*;
import com.be.back_end.service.BookingService.IBookingService;
import com.be.back_end.service.TranscationService.ITranscationService;
import com.be.back_end.service.TranscationService.IVNPayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Booking API", description = "Handles booking creation with file uploads")
public class BookingController {
    private final IBookingService bookingService;
    private final IVNPayService ivnPayService;
    public BookingController(IBookingService bookingService, IVNPayService ivnPayService) {
        this.bookingService = bookingService;
        this.ivnPayService = ivnPayService;
    }


    @PostMapping()
    public ResponseEntity<?> createBooking(@RequestBody BookingCreateRequest request, HttpServletRequest httpServletRequest) {
        try {
            BookingCreateResponse createdBooking = bookingService.createBooking(request, httpServletRequest);
            return ResponseEntity.ok(new ApiResponse<>(200, createdBooking, "Booking created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse(400, "Booking creation failed", List.of(e.getMessage()))
            );
        }
    }
    @GetMapping
    public ResponseEntity<?> getAllBookings(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (page < 1 || size <= 0) {
            ErrorResponse errorResponse = new ErrorResponse(
                    400,
                    "Invalid page or size",
                    List.of("Page and size must be positive values")
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        PaginatedResponseDTO<BookingResponse> paginatedBookings = bookingService.getAllBookings(page, size);

        if (paginatedBookings.getContent().isEmpty()) {
            ApiResponse<?> apiResponse = new ApiResponse<>(200, paginatedBookings, "No bookings found");
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }

        ApiResponse<?> apiResponse = new ApiResponse<>(200, paginatedBookings, "Bookings retrieved successfully");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }








}
