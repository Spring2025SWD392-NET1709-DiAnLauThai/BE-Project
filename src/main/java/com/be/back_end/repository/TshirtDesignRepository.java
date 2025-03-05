package com.be.back_end.repository;

import com.be.back_end.model.TshirtDesign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TshirtDesignRepository extends JpaRepository<TshirtDesign, String> {


    Optional<TshirtDesign> findByDesign_Id(String designId);
}
