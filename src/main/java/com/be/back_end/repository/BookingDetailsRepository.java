package com.be.back_end.repository;

import com.be.back_end.model.Bookingdetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface BookingDetailsRepository extends JpaRepository<Bookingdetails,String> {

    Page<Bookingdetails> findByBookingId(String bookingId, Pageable pageable);
}

