package com.be.back_end.controller;

import com.be.back_end.dto.BookingitemsDTO;
import com.be.back_end.model.Bookingitems;
import com.be.back_end.service.BookingItemService.IBookingitemsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orderitems")
public class BookingitemsController {
    private final IBookingitemsService bookingitemsService;

    public BookingitemsController(IBookingitemsService bookingitemsService) {
        this.bookingitemsService = bookingitemsService;
    }

    @PostMapping
    public ResponseEntity<String> addBookingItem(@RequestBody BookingitemsDTO dto) {
        if (bookingitemsService.createBookingItem(dto)) {
            return ResponseEntity.ok("Order item added");
        }
        return ResponseEntity.badRequest().body("Failed to add order item");
    }

    @GetMapping
    public ResponseEntity<List<Bookingitems>> getAllBookingItems() {
        return ResponseEntity.ok(bookingitemsService.getAllBookingItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bookingitems> getBookingItemById(@PathVariable String id) {
        Bookingitems orderItem = bookingitemsService.getBookingItemById(id);
        return orderItem != null ? ResponseEntity.ok(orderItem) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bookingitems> updateBookingItem(@PathVariable String id, @RequestBody BookingitemsDTO dto) {
        Bookingitems updatedOrderItem = bookingitemsService.updateBookingItem(id, dto);
        return updatedOrderItem != null ? ResponseEntity.ok(updatedOrderItem) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBookingItem(@PathVariable String id) {
        return bookingitemsService.deleteBookingItem(id) ? ResponseEntity.ok("Order item deleted") : ResponseEntity.badRequest().body("Order item not found");
    }
}