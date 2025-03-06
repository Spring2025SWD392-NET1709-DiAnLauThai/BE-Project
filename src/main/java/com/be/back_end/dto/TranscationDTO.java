package com.be.back_end.dto;

import com.be.back_end.model.Bookings;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TranscationDTO {
    private String id;
    private String bookingId;
    private String transactionName;
    private String transactionMethod;
    private LocalDateTime transactionDate;
    private BigDecimal transactionAmount;
    private String transactionStatus;
    private String bankCode;
    private String reason;
    private String transactionType;
}
