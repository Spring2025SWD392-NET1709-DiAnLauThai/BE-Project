package com.be.back_end.service.FeedbackService;

import com.be.back_end.dto.request.FeedbackCreateRequest;
import com.be.back_end.dto.request.FeedbacksRequest;
import com.be.back_end.dto.response.TshirtFeedbackResponse;
import com.be.back_end.model.Feedback;

import java.util.List;

public interface IFeedbackService {
    boolean createFeedback(FeedbackCreateRequest feedbackCreateRequest);
    List<TshirtFeedbackResponse> getAllSystemFeedbacks();
    Feedback getFeedbackById(String id);
    Feedback updateFeedback(String id, FeedbackCreateRequest feedbackCreateRequest);
    boolean deleteFeedback(String id);
    boolean createSystemFeedback(FeedbackCreateRequest feedbackCreateRequest);
    List<TshirtFeedbackResponse> getAllTshirtsFeedbacksById(String  tshirtId);
}
