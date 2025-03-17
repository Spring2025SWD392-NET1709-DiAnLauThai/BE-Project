package com.be.back_end.service.FeedbackService;

import com.be.back_end.dto.request.FeedbackCreateRequest;
import com.be.back_end.enums.FeedbackTypeEnums;
import com.be.back_end.model.*;
import com.be.back_end.repository.BookingRepository;
import com.be.back_end.repository.FeedbackRepository;
import com.be.back_end.repository.TshirtsFeedbackRepository;
import com.be.back_end.utils.AccountUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService implements IFeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final BookingRepository bookingRepository;
    private final AccountUtils accountUtils;
    private final TshirtsFeedbackRepository tshirtsFeedbackRepository;
    public FeedbackService(FeedbackRepository feedbackRepository, BookingRepository bookingRepository, AccountUtils accountUtils, TshirtsFeedbackRepository tshirtsFeedbackRepository) {
        this.feedbackRepository = feedbackRepository;
        this.bookingRepository = bookingRepository;

        this.accountUtils = accountUtils;
        this.tshirtsFeedbackRepository = tshirtsFeedbackRepository;
    }

    @Override
    public boolean createFeedback(FeedbackCreateRequest feedbackCreateRequest) {
        Account account= accountUtils.getCurrentAccount();
        if(account==null){
            return  false;
        }
        Tshirts  tshirts= new Tshirts();
        Feedback feedback = new Feedback();
        TshirtFeedback tshirtFeedback= new TshirtFeedback();
        feedback.setUser(account);
        feedback.setType(FeedbackTypeEnums.T_SHIRT);
        feedback.setRating(feedbackCreateRequest.getRating());
        feedback.setDetail(feedbackCreateRequest.getDetail());
        feedbackRepository.save(feedback);
        tshirtFeedback.setTshirt(tshirts);
        tshirtFeedback.setFeedback(feedback);
        tshirtsFeedbackRepository.save(tshirtFeedback);
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
