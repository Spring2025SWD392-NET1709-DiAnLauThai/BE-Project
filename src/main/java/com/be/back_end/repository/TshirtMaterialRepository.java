package com.be.back_end.repository;

import com.be.back_end.model.TShirtMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TshirtMaterialRepository extends JpaRepository<TShirtMaterial,String> {
}
