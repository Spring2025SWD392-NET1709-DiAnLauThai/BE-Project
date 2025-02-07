package com.be.back_end.repository;

import com.be.back_end.model.Tshirts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TshirtRepository extends JpaRepository<Tshirts, Integer> {
    List<Tshirts> findByName(String name);
}
