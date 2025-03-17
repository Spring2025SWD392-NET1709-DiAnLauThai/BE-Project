package com.be.back_end.dto.request;

import com.be.back_end.enums.FeedbackTypeEnums;
import lombok.Data;

@Data
public class FeedbackCreateRequest {
    private String tshirtId;
    private int rating;
    private String detail;

}
