package com.be.back_end.service.FeedbackService;

import com.be.back_end.dto.FeedbackDTO;
import com.be.back_end.model.Feedback;

import java.util.List;

public interface IFeedbackService {
    boolean createFeedback(FeedbackDTO feedbackDTO);
    List<Feedback> getAllFeedbacks();
    Feedback getFeedbackById(String id);
    Feedback updateFeedback(String id, FeedbackDTO feedbackDTO);
    boolean deleteFeedback(String id);
}
