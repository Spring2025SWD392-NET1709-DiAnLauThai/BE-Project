package com.be.back_end.service.FeedbackService;

import com.be.back_end.dto.request.FeedbackCreateRequest;
import com.be.back_end.dto.request.FeedbacksRequest;
import com.be.back_end.dto.response.TshirtFeedbackResponse;
import com.be.back_end.enums.FeedbackTypeEnums;
import com.be.back_end.model.*;
import com.be.back_end.repository.BookingRepository;
import com.be.back_end.repository.FeedbackRepository;
import com.be.back_end.repository.TshirtsFeedbackRepository;
import com.be.back_end.repository.TshirtsRepository;
import com.be.back_end.utils.AccountUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackService implements IFeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final BookingRepository bookingRepository;
    private final AccountUtils accountUtils;
    private final TshirtsFeedbackRepository tshirtsFeedbackRepository;
    private final TshirtsRepository tshirtsRepository;
    public FeedbackService(FeedbackRepository feedbackRepository, BookingRepository bookingRepository, AccountUtils accountUtils, TshirtsFeedbackRepository tshirtsFeedbackRepository, TshirtsRepository tshirtsRepository) {
        this.feedbackRepository = feedbackRepository;
        this.bookingRepository = bookingRepository;

        this.accountUtils = accountUtils;
        this.tshirtsFeedbackRepository = tshirtsFeedbackRepository;
        this.tshirtsRepository = tshirtsRepository;
    }

    @Override
    public boolean createFeedback(FeedbackCreateRequest feedbackCreateRequest) {
        Account account= accountUtils.getCurrentAccount();
        if(account==null){
            return  false;
        }
        Tshirts tshirt = tshirtsRepository.findById(feedbackCreateRequest.getTshirtId()).orElse(null);
        if (tshirt == null) {
            return false;
        }
        boolean feedbackExists = tshirtsFeedbackRepository.existsByFeedback_User_IdAndTshirt_Id(account.getId(), feedbackCreateRequest.getTshirtId());
        if (feedbackExists) {
            return false;
        }
        Feedback feedback = new Feedback();
        TshirtFeedback tshirtFeedback= new TshirtFeedback();
        feedback.setUser(account);
        feedback.setType(FeedbackTypeEnums.T_SHIRT);
        feedback.setRating(feedbackCreateRequest.getRating());
        feedback.setDetail(feedbackCreateRequest.getDetail());
        feedbackRepository.save(feedback);
        tshirtFeedback.setTshirt(tshirt);
        tshirtFeedback.setFeedback(feedback);
        tshirtsFeedbackRepository.save(tshirtFeedback);
        return true;
    }
    @Override
    public boolean createSystemFeedback(FeedbackCreateRequest feedbackCreateRequest) {
        Account account= accountUtils.getCurrentAccount();
        if(account==null){
            return  false;
        }
        Feedback feedback = new Feedback();
        feedback.setUser(account);
        feedback.setType(FeedbackTypeEnums.SYSTEM);
        feedback.setRating(feedbackCreateRequest.getRating());
        feedback.setDetail(feedbackCreateRequest.getDetail());
        feedbackRepository.save(feedback);
        return true;
    }
    @Override
    public List<TshirtFeedbackResponse> getAllTshirtsFeedbacksById(FeedbacksRequest feedbacksRequest)
    {
        List<Feedback> feedbackList= feedbackRepository.findByTshirtFeedbacks_Tshirt_Id(feedbacksRequest.getTshirtid());
        List<TshirtFeedbackResponse> feedbackResponseList= new ArrayList<>();
        for(Feedback feedback:feedbackList){
            TshirtFeedbackResponse tshirtFeedbackResponse=new TshirtFeedbackResponse();
            tshirtFeedbackResponse.setFeedbackId(feedback.getId());
            tshirtFeedbackResponse.setDetail(feedback.getDetail());
            tshirtFeedbackResponse.setUsername(feedback.getUser().getName());
            tshirtFeedbackResponse.setRating(feedback.getRating());
            tshirtFeedbackResponse.setCreateddate(feedback.getCreateddate());
            feedbackResponseList.add(tshirtFeedbackResponse);
        }
        return feedbackResponseList;
    }
    @Override
    public List<TshirtFeedbackResponse> getAllSystemFeedbacks()
    {
        List<Feedback> feedbackList= feedbackRepository.findByType(FeedbackTypeEnums.SYSTEM);
        List<TshirtFeedbackResponse> feedbackResponseList= new ArrayList<>();
        for(Feedback feedback:feedbackList){
            TshirtFeedbackResponse tshirtFeedbackResponse=new TshirtFeedbackResponse();
            tshirtFeedbackResponse.setFeedbackId(feedback.getId());
            tshirtFeedbackResponse.setDetail(feedback.getDetail());
            tshirtFeedbackResponse.setUsername(feedback.getUser().getName());
            tshirtFeedbackResponse.setRating(feedback.getRating());
            tshirtFeedbackResponse.setCreateddate(feedback.getCreateddate());
            feedbackResponseList.add(tshirtFeedbackResponse);
        }
        return feedbackResponseList;
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
