package com.be.back_end.repository;


import com.be.back_end.model.Account;
import com.be.back_end.model.Designs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DesignRepository extends JpaRepository<Designs, String>{

}
