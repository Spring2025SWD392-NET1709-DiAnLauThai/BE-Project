package com.be.back_end.repository;

import com.be.back_end.model.Bookingdetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingDetailsRepository extends JpaRepository<Bookingdetails,String> {
}
