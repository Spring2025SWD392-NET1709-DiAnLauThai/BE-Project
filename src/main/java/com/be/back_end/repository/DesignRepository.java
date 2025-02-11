package com.be.back_end.repository;

import com.be.back_end.model.Designs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DesignRepository extends JpaRepository<Designs, Integer> {

}
