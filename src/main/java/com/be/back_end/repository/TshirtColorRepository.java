package com.be.back_end.repository;

import com.be.back_end.model.TShirtColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TshirtColorRepository extends JpaRepository<TShirtColor,String> {
    void deleteByTshirtId(String tshirtId);
}
