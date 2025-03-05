package com.be.back_end.controller;

import com.be.back_end.dto.request.BookingCreateRequest;
import com.be.back_end.dto.response.ApiResponse;
import com.be.back_end.dto.response.BookingCreateResponse;
import com.be.back_end.dto.response.ErrorResponse;
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







}
