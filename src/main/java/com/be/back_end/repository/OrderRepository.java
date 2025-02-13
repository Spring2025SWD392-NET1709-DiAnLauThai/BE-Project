package com.be.back_end.repository;


import com.be.back_end.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Orders, String> {
}
