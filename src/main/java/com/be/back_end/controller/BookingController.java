package com.be.back_end.controller;

import com.be.back_end.dto.BookingDTO;
import com.be.back_end.dto.response.ApiResponse;
import com.be.back_end.dto.response.ErrorResponse;
import com.be.back_end.model.Bookings;
import com.be.back_end.service.BookingService.IBookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final IBookingService bookingService;

    public BookingController(IBookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<?> addbooking(@RequestBody BookingDTO bookingDTO) {
        boolean isCreated = bookingService.createbooking(bookingDTO);

        if (isCreated) {
            return ResponseEntity.ok(new ApiResponse<>(200, List.of("Booking added successfully"), "Success"));
        } else {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse(400, "Booking creation failed", List.of("Invalid booking details or account issue"))
            );
        }
    }

   /* @GetMapping
    public ResponseEntity<List<Bookings>> getAllbookings() {
        return ResponseEntity.ok(bookingService.getAllbookings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bookings> getbookingById(@PathVariable String id) {
        Bookings booking = bookingService.getbookingById(id);
        return booking != null ? ResponseEntity.ok(booking) : ResponseEntity.notFound().build();
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletebooking(@PathVariable String id) {
        return bookingService.deletebooking(id) ? ResponseEntity.ok("Booking deleted") : ResponseEntity.badRequest().body("Booking not found");
    }*/
}