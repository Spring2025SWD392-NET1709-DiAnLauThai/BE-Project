package com.be.back_end.dto.response;

import com.be.back_end.enums.BookingEnums;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BookingResponseNoLinkDTO {
    private String designerName;
    private BigDecimal totalPrice;
    private int totalQuantity;
    private BookingEnums bookingStatus;
    private LocalDateTime datecreated;
    private BigDecimal depositAmount;
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
        private String tshirtName;
        private String tshirtDescription;
        private String imageUrl;
        private String imageFile;
        private List<ColorResponse> colors;
        private BigDecimal unitPrice;
    }

    // New class to represent Color details
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ColorResponse {
        private String colorId;
        private String colorName;
        private String colorCode;
    }
}