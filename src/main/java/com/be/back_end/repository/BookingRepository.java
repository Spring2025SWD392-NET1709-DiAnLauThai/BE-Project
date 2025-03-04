package com.be.back_end.repository;


import com.be.back_end.model.Account;
import com.be.back_end.model.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Bookings, String> {
    List<Bookings> findByaccount(Account account);
}
