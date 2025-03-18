package com.be.back_end.controller;

import com.be.back_end.dto.request.BookingCreateRequest;
import com.be.back_end.dto.request.CancelBookingRequest;
import com.be.back_end.dto.request.PublicTshirtRequest;
import com.be.back_end.dto.response.*;
import com.be.back_end.service.BookingService.IBookingService;
import com.be.back_end.service.TranscationService.IVNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
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
    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<?> cancelBooking(@RequestBody CancelBookingRequest cancelBookingRequest) {
        try {
            boolean isCancelled = bookingService.cancelBooking(cancelBookingRequest);
            if (isCancelled) {
                return ResponseEntity.ok(new ApiResponse<>(200, true, "Booking cancelled successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse(500, "Booking cancellation failed", List.of("Unexpected error during cancellation.")));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(404, "Booking not found", List.of(e.getMessage())));
        }
    }
    @PutMapping("/{bookingId}/payment")
    public ResponseEntity<?> payRemainDeposit(@PathVariable String bookingId, HttpServletRequest request) {
        try {
            String paymentUrl = bookingService.generateFullyPaidUrl(bookingId, request);
            if (paymentUrl == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse(500, "Failed to generate payment URL", List.of("VNPay URL generation returned null.")));
            }

            return ResponseEntity.ok(new ApiResponse<>(200, paymentUrl, "Payment URL generated successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(404, "Booking not found", List.of(e.getMessage())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "An unexpected error occurred", List.of(e.getMessage())));
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

    @PutMapping("/{bookingId}/public")
    public ResponseEntity<?> publicTshirt(@RequestBody @Valid PublicTshirtRequest request) {
        try {
            boolean isPublic = bookingService.publicTshirt(request);
            if (isPublic) {
                return ResponseEntity.ok(new ApiResponse<>(200, true, "Booking made public successfully"));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "Failed to make booking public", List.of("Unexpected error occurred.")));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(404, "Booking not found", List.of(e.getMessage())));
        }
    }






}
