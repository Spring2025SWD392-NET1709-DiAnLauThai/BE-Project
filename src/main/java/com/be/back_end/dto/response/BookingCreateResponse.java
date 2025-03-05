package com.be.back_end.dto.response;

import com.be.back_end.enums.BookingEnums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreateResponse {
    private String bookingId;
    private BigDecimal totalPrice;
    private int totalQuantity;
    private String accountId;
    private String code;
    private String title;
    private int duration;
    private BookingEnums status;
    private LocalDateTime dateCreated;
    private LocalDateTime lastUpdated;
    private List<BookingDetailResponse> bookingDetails;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookingDetailResponse {
        private String bookingDetailId;
        private String designId;
        private String designFile;  // Changed from designName
        private String description; // Added from Bookingdetails model

        private BigDecimal unitPrice;
    }
}

