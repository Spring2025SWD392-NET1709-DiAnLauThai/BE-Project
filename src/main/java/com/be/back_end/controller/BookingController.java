package com.be.back_end.controller;

import com.be.back_end.dto.BookingDTO;
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
    public ResponseEntity<String> addbooking(@RequestBody BookingDTO bookingDTO) {
        if (bookingService.createbooking(bookingDTO)) {
            return ResponseEntity.ok("Booking added");
        }
        return ResponseEntity.badRequest().body("Booking failed to add");
    }

    @GetMapping
    public ResponseEntity<List<Bookings>> getAllbookings() {
        return ResponseEntity.ok(bookingService.getAllbookings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bookings> getbookingById(@PathVariable String id) {
        Bookings booking = bookingService.getbookingById(id);
        return booking != null ? ResponseEntity.ok(booking) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bookings> updatebooking(@PathVariable String id, @RequestBody BookingDTO bookingDTO) {
        Bookings updatedbooking = bookingService.updatebooking(id, bookingDTO);
        return updatedbooking != null ? ResponseEntity.ok(updatedbooking) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletebooking(@PathVariable String id) {
        return bookingService.deletebooking(id) ? ResponseEntity.ok("Booking deleted") : ResponseEntity.badRequest().body("Booking not found");
    }
}