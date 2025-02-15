package com.be.back_end.controller;

import com.be.back_end.dto.FeedbackDTO;
import com.be.back_end.model.Feedback;
import com.be.back_end.service.FeedbackService.IFeedbackService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {
    private final IFeedbackService feedbackService;

    public FeedbackController(IFeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public String addFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        return feedbackService.createFeedback(feedbackDTO) ? "Feedback added" : "Failed to add feedback";
    }

    @GetMapping
    public List<Feedback> getAllFeedbacks() {
        return feedbackService.getAllFeedbacks();
    }

    @GetMapping("/{id}")
    public Feedback getFeedbackById(@PathVariable String id) {
        return feedbackService.getFeedbackById(id);
    }

    @PutMapping("/{id}")
    public Feedback updateFeedback(@PathVariable String id, @RequestBody FeedbackDTO feedbackDTO) {
        return feedbackService.updateFeedback(id, feedbackDTO);
    }

    @DeleteMapping("/{id}")
    public String deleteFeedback(@PathVariable String id) {
        return feedbackService.deleteFeedback(id) ? "Feedback deleted" : "Failed to delete feedback";
    }
}
