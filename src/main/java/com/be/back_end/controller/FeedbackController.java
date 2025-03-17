package com.be.back_end.controller;

import com.be.back_end.dto.request.FeedbackCreateRequest;
import com.be.back_end.dto.response.ApiResponse;
import com.be.back_end.model.Feedback;
import com.be.back_end.service.FeedbackService.IFeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {
    private final IFeedbackService feedbackService;

    public FeedbackController(IFeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/tshirt")
    public ResponseEntity<?> addFeedback(@RequestBody FeedbackCreateRequest feedbackCreateRequest) {
        if(feedbackService.createFeedback(feedbackCreateRequest)){
            return ResponseEntity.status(200).body(new ApiResponse<>(200,null,"Feedback added"));
        }
        return ResponseEntity.status(400).body(new ApiResponse<>(200,null,"Feedback added"));
    }

    /*@GetMapping
    public List<Feedback> getAllFeedbacks() {
        return feedbackService.getAllFeedbacks();
    }

    @GetMapping("/{id}")
    public Feedback getFeedbackById(@PathVariable String id) {
        return feedbackService.getFeedbackById(id);
    }

    @PutMapping("/{id}")
    public Feedback updateFeedback(@PathVariable String id, @RequestBody FeedbackCreateRequest feedbackCreateRequest) {
        return feedbackService.updateFeedback(id, feedbackCreateRequest);
    }*/


}
