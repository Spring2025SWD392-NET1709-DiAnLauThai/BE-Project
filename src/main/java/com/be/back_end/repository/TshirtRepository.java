package com.be.back_end.repository;

import com.be.back_end.model.Tshirts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TshirtRepository extends JpaRepository<Tshirts, UUID> {
    List<Tshirts> findByName(String name);
}
