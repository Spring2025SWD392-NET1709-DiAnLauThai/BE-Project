package com.be.back_end.dto.response;

import com.be.back_end.model.Bookings;
import com.be.back_end.model.Task;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BookingResponse {
    private String id;
    private BigDecimal totalPrice;
    private int totalQuantity;
    private String status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime updateDate;
    private LocalDateTime createdDate;
    private String code;
    private String title;
    private BigDecimal depositAmount;
    private String assignedDesigner;


}
