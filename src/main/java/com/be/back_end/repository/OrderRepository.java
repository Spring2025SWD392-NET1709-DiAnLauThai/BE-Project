package com.be.back_end.repository;


import com.be.back_end.model.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Bookings, String> {
}
