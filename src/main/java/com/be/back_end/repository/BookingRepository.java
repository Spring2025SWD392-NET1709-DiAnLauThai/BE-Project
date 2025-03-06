package com.be.back_end.repository;


import com.be.back_end.model.Bookings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Bookings, String> {
    @Query("SELECT COALESCE(SUM(b.unit_price), 0) FROM Bookingdetails b WHERE b.booking.id = :bookingId")
    BigDecimal getTotalPriceByBookingId(@Param("bookingId") String bookingId);

    // Count total quantity (number of booking details) for a given booking ID
    @Query("SELECT COUNT(b) FROM Bookingdetails b WHERE b.booking.id = :bookingId")
    Integer getTotalQuantityByBookingId(@Param("bookingId") String bookingId);

    boolean existsByCode(String code);

    Optional<Bookings> findByCode(String code);

    Page<Bookings> findAll(Pageable pageable);
}
