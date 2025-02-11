package com.be.back_end.repository;

import com.be.back_end.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByUsername(String username);
    User findByEmail(String email);
}
