package com.be.back_end.repository;


import com.be.back_end.model.TshirtFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TshirtsFeedbackRepository extends JpaRepository<TshirtFeedback,String> {
    boolean existsByFeedback_User_IdAndTshirt_Id(String accountId, String tshirtId);
}
