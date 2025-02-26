package com.be.back_end.controller;

import com.be.back_end.dto.BookingDTO;
import com.be.back_end.model.Bookings;
import com.be.back_end.service.BookingService.IBookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class BookingController {
    private final IBookingService bookingService;

    public BookingController(IBookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<String> addBooking(@RequestBody BookingDTO bookingDTO) {
        if (bookingService.createBooking(bookingDTO)) {
            return ResponseEntity.ok("Order added");
        }
        return ResponseEntity.badRequest().body("Order failed to add");
    }

    @GetMapping
    public ResponseEntity<List<Bookings>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bookings> getBookingById(@PathVariable String id) {
        Bookings order = bookingService.getBookingById(id);
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bookings> updateBooking(@PathVariable String id, @RequestBody BookingDTO bookingDTO) {
        Bookings updatedOrder = bookingService.updateBooking(id, bookingDTO);
        return updatedOrder != null ? ResponseEntity.ok(updatedOrder) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable String id) {
        return bookingService.deleteBooking(id) ? ResponseEntity.ok("Order deleted") : ResponseEntity.badRequest().body("Order not found");
    }
}