package com.be.back_end.dto.response;

import com.be.back_end.enums.BookingEnums;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BookingResponseNoLinkDTO {

    private BigDecimal totalPrice;
    private int totalQuantity;
    private BookingEnums bookingStatus;
    private LocalDateTime datecreated;

    private LocalDateTime updateddate;
    private LocalDateTime startdate;
    private LocalDateTime enddate;
    private String code;
    private String title;
    private List<BookingDetailResponse> bookingDetails;
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookingDetailResponse {
        private String bookingDetailId;
        private String designFile;
        private String description;

        private BigDecimal unitPrice;
    }

}
