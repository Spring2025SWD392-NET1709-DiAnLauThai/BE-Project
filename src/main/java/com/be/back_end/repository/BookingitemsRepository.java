package com.be.back_end.repository;

import com.be.back_end.model.Bookingitems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingitemsRepository extends JpaRepository<Bookingitems,String> {
}
