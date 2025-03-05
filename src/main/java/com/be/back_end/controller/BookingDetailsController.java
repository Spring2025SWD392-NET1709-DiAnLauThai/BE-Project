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

}