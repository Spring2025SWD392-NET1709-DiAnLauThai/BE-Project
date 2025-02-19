package com.be.back_end.controller;

import com.be.back_end.dto.BookingdetailsDTO;

import com.be.back_end.model.Bookingdetails;
import com.be.back_end.service.BookingDetailService.IBookingdetailService;
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

    @PostMapping
    public ResponseEntity<String> addbookingdetail(@RequestBody BookingdetailsDTO dto) {
        if (bookingdetailService.createbookingdetail(dto)) {
            return ResponseEntity.ok("Order item added");
        }
        return ResponseEntity.badRequest().body("Failed to add order item");
    }

    @GetMapping
    public ResponseEntity<List<Bookingdetails>> getAllbookingdetails() {
        return ResponseEntity.ok(bookingdetailService.getAllbookingdetails());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bookingdetails> getbookingdetailById(@PathVariable String id) {
        Bookingdetails bookingdetail = bookingdetailService.getbookingdetailById(id);
        return bookingdetail != null ? ResponseEntity.ok(bookingdetail) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bookingdetails> updatebookingdetail(@PathVariable String id, @RequestBody BookingdetailsDTO dto) {
        Bookingdetails updatedbookingdetail = bookingdetailService.updatebookingdetail(id, dto);
        return updatedbookingdetail != null ? ResponseEntity.ok(updatedbookingdetail) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletebookingdetail(@PathVariable String id) {
        return bookingdetailService.deletebookingdetail(id) ? ResponseEntity.ok("Order item deleted") : ResponseEntity.badRequest().body("Order item not found");
    }
}