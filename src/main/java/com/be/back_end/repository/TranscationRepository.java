package com.be.back_end.repository;


import com.be.back_end.model.Bookings;
import com.be.back_end.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TranscationRepository extends JpaRepository<Transaction, String> {



    @Query(value = "SELECT t.* FROM transaction t " +
            "INNER JOIN bookings b ON t.bookingid = b.bookingid " +
            "WHERE b.accountid = :accountId " +
            "ORDER BY t.transactiondate",
            countQuery = "SELECT COUNT(t.transactionid) FROM transaction t " +
                    "INNER JOIN bookings b ON t.bookingid = b.bookingid " +
                    "WHERE b.accountid = :accountId",
            nativeQuery = true)
    Page<Transaction> findAllByAccountId(@Param("accountId") String accountId, Pageable pageable);
    Page<Transaction> findAll(Pageable pageable);


    Optional<Transaction> findByBookings_Id(String bookingId);

}
