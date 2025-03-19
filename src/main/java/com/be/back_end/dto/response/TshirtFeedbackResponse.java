package com.be.back_end.dto.response;

import com.be.back_end.model.Account;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class TshirtFeedbackResponse {
    private String feedbackId;
    private int rating;
    private LocalDateTime Createddate;
    private String detail;
    private String username;
}
