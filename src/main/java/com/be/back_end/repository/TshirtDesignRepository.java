package com.be.back_end.repository;

import com.be.back_end.model.TshirtDesign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TshirtDesignRepository extends JpaRepository<TshirtDesign, String> {
}
