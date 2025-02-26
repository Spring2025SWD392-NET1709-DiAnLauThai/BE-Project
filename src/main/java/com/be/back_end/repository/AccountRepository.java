package com.be.back_end.repository;

import com.be.back_end.enums.RoleEnums;
import com.be.back_end.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByEmail(String email);
    Optional<Account> findById(String id);

    Page<Account> findByEmailContainingIgnoreCase(String email, Pageable pageable);
    Page<Account> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Account> findByPhone(String phone, Pageable pageable);
    Page<Account> findByCreatedAtBetween(LocalDateTime dateFrom, LocalDateTime dateTo, Pageable pageable);
    Page<Account> findByRole(String role,Pageable pageable);
}
