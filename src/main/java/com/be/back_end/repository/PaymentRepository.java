package com.be.back_end.repository;

import com.be.back_end.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Transaction, String> {
}
