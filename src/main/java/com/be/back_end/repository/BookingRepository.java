package com.be.back_end.repository;


import com.be.back_end.enums.BookingEnums;
import com.be.back_end.model.Account;
import com.be.back_end.model.Bookings;

import com.be.back_end.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Bookings, String> {


    boolean existsByCode(String code);

    Optional<Bookings> findByCode(String code);

    List<Bookings> findAllByAccount(Account account);
    Bookings findByTransaction(Transaction transaction);
    Page<Bookings> findAll(Pageable pageable);
    Page<Bookings> findAllByAccount(Account account,Pageable pageable);

    List<Bookings> findByStatusNotAndEnddateBefore(BookingEnums status, LocalDateTime currentDate);
}
