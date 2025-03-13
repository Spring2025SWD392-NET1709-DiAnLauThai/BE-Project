package com.be.back_end.repository;

import com.be.back_end.model.RefundForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefundRequestRepository extends JpaRepository<RefundForm,String> {
}
