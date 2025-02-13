package com.be.back_end.repository;


import com.be.back_end.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MaterialRepository extends JpaRepository<Material, String> {
}
