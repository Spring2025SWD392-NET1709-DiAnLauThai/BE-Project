package com.be.back_end.repository;


import com.be.back_end.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

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

    Optional<Transaction> findByBooking_Id(String bookingId);

    @Query("SELECT t " +
            "FROM Transaction t " +
            "WHERE t.transactionDate >= :startDate AND t.transactionDate < :endDate " +
                "AND UPPER(t.transactionStatus) = 'FULLY_PAID'")
    List<Transaction> findFullyPaidTransactionsByDateRange(@Param("startDate") LocalDateTime startDate,
                                                           @Param("endDate") LocalDateTime endDate);

}
