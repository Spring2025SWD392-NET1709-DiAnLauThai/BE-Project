package com.be.back_end.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccountRepository  {
    List<String> findByUsername(String username);
    String findByEmail(String email);
}
