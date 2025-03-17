package com.be.back_end.service.FeedbackService;

import com.be.back_end.dto.request.FeedbackCreateRequest;
import com.be.back_end.enums.FeedbackTypeEnums;
import com.be.back_end.model.Bookings;
import com.be.back_end.model.Feedback;
import com.be.back_end.repository.BookingRepository;
import com.be.back_end.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService implements IFeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final BookingRepository bookingRepository;
    public FeedbackService(FeedbackRepository feedbackRepository, BookingRepository bookingRepository) {
        this.feedbackRepository = feedbackRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public boolean createFeedback(FeedbackCreateRequest feedbackCreateRequest) {
        Feedback feedback = new Feedback();


        feedback.setType(FeedbackTypeEnums.T_SHIRT);
        feedback.setRating(feedbackCreateRequest.getRating());
        feedback.setDetail(feedbackCreateRequest.getDetail());
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
    public Feedback updateFeedback(String id, FeedbackCreateRequest feedbackCreateRequest) {
        Feedback feedback = feedbackRepository.findById(id).orElse(null);
        if (feedback != null) {
            feedback.setType(feedbackCreateRequest.getType());
            feedback.setRating(feedbackCreateRequest.getRating());
            feedback.setDetail(feedbackCreateRequest.getDetail());
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
