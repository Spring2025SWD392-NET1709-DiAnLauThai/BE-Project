package com.be.back_end.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class BookingDetailResponseDTO {
    private String bookingDetailId;
    private String designFile;
    private String description;
    private BigDecimal unitPrice;
    public BookingDetailResponseDTO(String bookingDetailId,String designFile, String description, BigDecimal unitPrice) {
        this.bookingDetailId = bookingDetailId;
        this.designFile = designFile;
        this.description = description;
        this.unitPrice = unitPrice;
    }
}
