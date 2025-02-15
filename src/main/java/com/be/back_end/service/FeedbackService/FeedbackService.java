package com.be.back_end.service.FeedbackService;

import com.be.back_end.dto.FeedbackDTO;
import com.be.back_end.model.Feedback;
import com.be.back_end.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService implements IFeedbackService {
    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public boolean createFeedback(FeedbackDTO feedbackDTO) {
        Feedback feedback = new Feedback();
        feedback.setType(feedbackDTO.getType());
        feedback.setRating(feedbackDTO.getRating());
        feedback.setDetail(feedbackDTO.getDetail());
        feedbackRepository.save(feedback);
        return true;
    }

    @Override
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    @Override
    public Feedback getFeedbackById(String id) {
        return feedbackRepository.findById(id).orElse(null);
    }

    @Override
    public Feedback updateFeedback(String id, FeedbackDTO feedbackDTO) {
        Feedback feedback = feedbackRepository.findById(id).orElse(null);
        if (feedback != null) {
            feedback.setType(feedbackDTO.getType());
            feedback.setRating(feedbackDTO.getRating());
            feedback.setDetail(feedbackDTO.getDetail());
            return feedbackRepository.save(feedback);
        }
        return null;
    }

    @Override
    public boolean deleteFeedback(String id) {
        if (feedbackRepository.existsById(id)) {
            feedbackRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
