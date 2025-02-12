package com.be.back_end.repository;

import com.be.back_end.model.Tshirts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TshirtsRepository extends JpaRepository<Tshirts,String> {
}
