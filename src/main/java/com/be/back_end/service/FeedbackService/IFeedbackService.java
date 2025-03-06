package com.be.back_end.service.FeedbackService;

import com.be.back_end.dto.request.FeedbackCreateRequest;
import com.be.back_end.model.Feedback;

import java.util.List;

public interface IFeedbackService {
    boolean createFeedback(FeedbackCreateRequest feedbackCreateRequest);
    List<Feedback> getAllFeedbacks();
    Feedback getFeedbackById(String id);
    Feedback updateFeedback(String id, FeedbackCreateRequest feedbackCreateRequest);
    boolean deleteFeedback(String id);
}
