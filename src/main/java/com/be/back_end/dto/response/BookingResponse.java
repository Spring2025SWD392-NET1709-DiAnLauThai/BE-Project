package com.be.back_end.dto.response;

import com.be.back_end.model.Bookings;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BookingResponse {
    private String id;
    private BigDecimal totalPrice;
    private int totalQuantity;
    private String status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String code;
    private String title;

    public BookingResponse(Bookings booking) {
        this.id = booking.getId();
        this.totalPrice = booking.getTotal_price();
        this.totalQuantity = booking.getTotal_quantity();
        this.status = booking.getStatus() != null ? booking.getStatus().toString() : null;
        this.startDate = booking.getStartdate();
        this.endDate = booking.getEnddate();
        this.code = booking.getCode();
        this.title = booking.getTitle();
    }
}
