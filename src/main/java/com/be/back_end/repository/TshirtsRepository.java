package com.be.back_end.repository;

import com.be.back_end.model.Account;
import com.be.back_end.model.Tshirts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TshirtsRepository extends JpaRepository<Tshirts,String> {
    Page<Tshirts> findByNameContainingIgnoreCaseAndAccount_Id( String name,String accountId, Pageable pageable);
    Page<Tshirts> findByCreatedAtBetweenAndAccount_Id(LocalDateTime dateFrom, LocalDateTime dateTo,String accountId, Pageable pageable);
    Page<Tshirts> findByAccount_Id(String accountId,Pageable pageable);
    List<Tshirts> findByBookingdetailsIsNullAndAccount_Id(String accountId);
    Page<Tshirts> findByNameContainingIgnoreCase( String name,Pageable pageable);
    Page<Tshirts> findByCreatedAtBetween(LocalDateTime dateFrom, LocalDateTime dateTo, Pageable pageable);
    Page<Tshirts> findByBookingdetails_Booking_IsPublicTrue(Pageable pageable);
    Page<Tshirts> findByBookingdetails_Booking_IsPublicTrueAndNameContainingIgnoreCase(
            String keyword, Pageable pageable);
    Page<Tshirts> findByBookingdetails_Booking_IsPublicTrueAndCreatedAtBetween(
            LocalDateTime dateFrom, LocalDateTime dateTo, Pageable pageable);
    Page<Tshirts> findByBookingdetails_Booking_IsPublicTrueAndNameContainingIgnoreCaseAndCreatedAtBetween(
            String keyword, LocalDateTime dateFrom, LocalDateTime dateTo, Pageable pageable);

    //Find all T-Shirt based on the date created in range
    @Query("SELECT t FROM Tshirts t WHERE t.createdAt >= :startDate AND t.createdAt < :endDate")
    List<Tshirts> findTshirtsCreatedBetween(@Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate);
}
