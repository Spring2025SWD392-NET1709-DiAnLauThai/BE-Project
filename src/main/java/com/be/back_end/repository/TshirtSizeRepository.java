package com.be.back_end.repository;

import com.be.back_end.model.TShirtSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TshirtSizeRepository extends JpaRepository<TShirtSize,String> {
}
