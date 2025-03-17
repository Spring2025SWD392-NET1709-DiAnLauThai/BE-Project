package com.be.back_end.repository;

import com.be.back_end.model.Account;
import com.be.back_end.model.Tshirts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
