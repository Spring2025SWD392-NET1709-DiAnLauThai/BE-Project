package com.be.back_end.repository;


import com.be.back_end.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranscationRepository extends JpaRepository<Transaction, String> {
}
