package com.be.back_end.repository;

import com.be.back_end.model.Orderitems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderitemsRepository extends JpaRepository<Orderitems,String> {
}
