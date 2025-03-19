package com.be.back_end.controller;

import com.be.back_end.dto.request.FeedbackCreateRequest;
import com.be.back_end.dto.request.FeedbacksRequest;
import com.be.back_end.dto.response.ApiResponse;
import com.be.back_end.dto.response.ErrorResponse;
import com.be.back_end.dto.response.TshirtFeedbackResponse;
import com.be.back_end.model.Feedback;
import com.be.back_end.service.FeedbackService.IFeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
        try {
            if (feedbackService.createFeedback(feedbackCreateRequest)) {
                return ResponseEntity.ok(new ApiResponse<>(200, null, "Feedback added successfully."));
            }
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, null, "Failed to add feedback."));
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(
                    500,
                    "An unexpected error occurred.",
                    Collections.singletonList(e.getMessage())
            );
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    @PostMapping("/system")
    public ResponseEntity<?> addSystemFeedback(@RequestBody FeedbackCreateRequest feedbackCreateRequest) {
        try {
            if (feedbackService.createSystemFeedback(feedbackCreateRequest)) {
                return ResponseEntity.ok(new ApiResponse<>(200, null, "Feedback added successfully."));
            }
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, null, "Failed to add feedback."));
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(
                    500,
                    "An unexpected error occurred.",
                    Collections.singletonList(e.getMessage())
            );
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    @GetMapping("/tshirts/feedback")
    public ResponseEntity<?> getAllTshirtFeedback(@RequestBody FeedbacksRequest feedbacksRequest) {
        try {
            List<TshirtFeedbackResponse> feedbackResponses = feedbackService.getAllTshirtsFeedbacksById(feedbacksRequest);

            if (feedbackResponses.isEmpty()) {
                return ResponseEntity.noContent().build();  // 204 No Content
            }

            return ResponseEntity.ok(new ApiResponse<>(200, feedbackResponses, "Feedbacks retrieved successfully."));

        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(
                    500,
                    "An unexpected error occurred.",
                    List.of(e.getMessage())
            );
            return ResponseEntity.status(500).body(errorResponse);
        }
    }



}
