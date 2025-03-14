package com.be.back_end.repository;

import com.be.back_end.model.Account;
import com.be.back_end.model.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends JpaRepository<Color, String> {
}
