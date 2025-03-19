package com.be.back_end.repository;

import com.be.back_end.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, String> {
    List<Feedback> findByTshirtFeedbacks_Tshirt_Id(String tshirtId);
}
