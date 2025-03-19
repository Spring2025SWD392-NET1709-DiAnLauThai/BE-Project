package com.be.back_end.repository;

import com.be.back_end.enums.ActivationEnums;
import com.be.back_end.enums.RoleEnums;
import com.be.back_end.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    Optional<Account> findByEmail(String email);

    Page<Account> findByRole(RoleEnums role, Pageable pageable);

    Page<Account> findByCreatedAtBetween(LocalDateTime dateFrom, LocalDateTime dateTo, Pageable pageable);

    Page<Account> findByRoleAndCreatedAtBetween(RoleEnums role, LocalDateTime dateFrom, LocalDateTime dateTo, Pageable pageable);

    Page<Account> findByEmailContainingIgnoreCaseOrNameContainingIgnoreCase(String email, String name, Pageable pageable);

    Page<Account> findByRoleAndEmailContainingIgnoreCaseOrNameContainingIgnoreCase(RoleEnums role, String email, String name, Pageable pageable);

    Page<Account> findByCreatedAtBetweenAndEmailContainingIgnoreCaseOrNameContainingIgnoreCase(
            LocalDateTime dateFrom, LocalDateTime dateTo, String email, String name, Pageable pageable);

    Page<Account> findByRoleAndCreatedAtBetweenAndEmailContainingIgnoreCaseOrNameContainingIgnoreCase(
            RoleEnums role, LocalDateTime dateFrom, LocalDateTime dateTo, String email, String name, Pageable pageable);

    // 🔹 New Queries for Filtering by Activation Status
    Page<Account> findByStatus(ActivationEnums status, Pageable pageable);

    Page<Account> findByRoleAndStatus(RoleEnums role, ActivationEnums status, Pageable pageable);

    Page<Account> findByStatusAndCreatedAtBetween(ActivationEnums status, LocalDateTime dateFrom, LocalDateTime dateTo, Pageable pageable);

    Page<Account> findByRoleAndStatusAndCreatedAtBetween(RoleEnums role, ActivationEnums status, LocalDateTime dateFrom, LocalDateTime dateTo, Pageable pageable);

    List<Account> findByRoleAndStatusAndCreatedAtBetween(RoleEnums role, ActivationEnums status, LocalDateTime startDate, LocalDateTime endDate);

    Page<Account> findByRoleAndStatusAndEmailContainingIgnoreCaseOrNameContainingIgnoreCase(
            RoleEnums role, ActivationEnums status, String email, String name, Pageable pageable);

    Page<Account> findByRoleAndStatusAndCreatedAtBetweenAndEmailContainingIgnoreCaseOrNameContainingIgnoreCase(
            RoleEnums role, ActivationEnums status, LocalDateTime dateFrom, LocalDateTime dateTo, String email, String name, Pageable pageable);
}
