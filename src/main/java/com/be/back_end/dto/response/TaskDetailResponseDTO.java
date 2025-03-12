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
public class TaskDetailResponseDTO {
    private String designerName;
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
        private String description;
        private BigDecimal unitPrice;
        private DesignResponse design;
        private TShirtResponse tshirt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DesignResponse {
        private String designFile;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TShirtResponse {
        private String name;
        private String description;
        private String imageUrl;
        private List<TShirtColorResponse> tshirtColors;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TShirtColorResponse {
        private String colorName;
        private String colorCode;
    }
}
