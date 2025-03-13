package com.be.back_end.repository;

import com.be.back_end.model.ModificationForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModificationRequestRepository extends JpaRepository<ModificationForm,String> {
}
