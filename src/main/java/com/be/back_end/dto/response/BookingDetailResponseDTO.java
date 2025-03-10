package com.be.back_end.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class BookingDetailResponseDTO {
    private String bookingId;
    private String bookingDetailId;
    private String designId;
    private String designFile;
    private String description;
    private BigDecimal unitPrice;
    public BookingDetailResponseDTO(String bookingId,String bookingDetailId,String designId,String designFile, String description, BigDecimal unitPrice) {
        this.bookingId=bookingId;
        this.bookingDetailId = bookingDetailId;
        this.designFile = designFile;
        this.designId=designId;
        this.description = description;
        this.unitPrice = unitPrice;
    }
}