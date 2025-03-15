package com.be.back_end.repository;

import com.be.back_end.enums.BookingEnums;
import com.be.back_end.model.Bookingdetails;
import com.be.back_end.model.Bookings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BookingDetailsRepository extends JpaRepository<Bookingdetails,String> {

    Page<Bookingdetails> findByBookingId(String bookingId, Pageable pageable);

    List<Bookingdetails> findByBookingId(String bookingId);
    List<Bookingdetails> findByBooking(Bookings bookings);

    boolean existsByBookingIdAndTshirtIsNull(String bookingId);
    boolean existsByTshirtIdAndBooking_StatusNot(String tshirtId, BookingEnums status);

    Bookingdetails findByTshirtId(String tshirtId);
}

