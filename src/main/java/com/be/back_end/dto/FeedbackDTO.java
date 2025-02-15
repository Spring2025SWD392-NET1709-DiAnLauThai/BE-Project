package com.be.back_end.dto;

import com.be.back_end.enums.FeedbackTypeEnums;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class FeedbackDTO {
    private String id;
    private FeedbackTypeEnums type;
    private int rating;
    private String detail;

}
