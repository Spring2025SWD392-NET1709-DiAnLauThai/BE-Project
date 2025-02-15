package com.be.back_end.repository;


import com.be.back_end.model.Account;
import com.be.back_end.model.Designs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DesignRepository extends JpaRepository<Designs, String>{

}
