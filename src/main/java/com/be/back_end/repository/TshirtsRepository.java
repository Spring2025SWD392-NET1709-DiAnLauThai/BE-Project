package com.be.back_end.repository;

import com.be.back_end.model.Account;
import com.be.back_end.model.Tshirts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface TshirtsRepository extends JpaRepository<Tshirts,String> {
    Page<Tshirts> findByNameContainingIgnoreCaseAndBookingdetailsIsNull( String name, Pageable pageable);
    Page<Tshirts> findByCreatedAtBetweenAndBookingdetailsIsNull(LocalDateTime dateFrom, LocalDateTime dateTo, Pageable pageable);
    Page<Tshirts> findByBookingdetailsIsNull(Pageable pageable);
}
