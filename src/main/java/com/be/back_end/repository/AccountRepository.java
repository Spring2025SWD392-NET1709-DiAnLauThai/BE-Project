package com.be.back_end.repository;

import com.be.back_end.enums.ActivationEnums;
import com.be.back_end.enums.RoleEnums;
import com.be.back_end.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByEmail(String email);
    Page<Account> findByRole(RoleEnums role, Pageable pageable);
    Page<Account> findByStatus(ActivationEnums status, Pageable pageable);
    Page<Account> findByCreatedAtBetween(LocalDateTime dateFrom, LocalDateTime dateTo, Pageable pageable);
    Page<Account> findByRoleAndStatus(RoleEnums role, ActivationEnums status, Pageable pageable);
    Page<Account> findByRoleAndCreatedAtBetween(RoleEnums role, LocalDateTime dateFrom, LocalDateTime dateTo, Pageable pageable);
    Page<Account> findByStatusAndCreatedAtBetween(ActivationEnums status, LocalDateTime dateFrom, LocalDateTime dateTo, Pageable pageable);
    Page<Account> findByRoleAndStatusAndCreatedAtBetween(RoleEnums role, ActivationEnums status, LocalDateTime dateFrom, LocalDateTime dateTo, Pageable pageable);
    Page<Account> findByEmailContainingIgnoreCaseOrNameContainingIgnoreCase(String email, String name, Pageable pageable);
}
