package com.be.back_end.repository;

import com.be.back_end.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    List<User> findByUsername(String username);
    User findByEmail(String email);
}
