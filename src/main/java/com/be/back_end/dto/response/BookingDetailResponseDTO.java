package com.be.back_end.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookingDetailResponseDTO {
    private String bookingDetailId;
    private String bookingId;
    private String designId;
    private String designFile;
    private String description;
    private BigDecimal unitPrice;
    public BookingDetailResponseDTO(String bookingDetailId, String bookingId, String designId, String designFile, String description, BigDecimal unitPrice) {
        this.bookingDetailId = bookingDetailId;
        this.bookingId = bookingId;
        this.designId = designId;
        this.designFile = designFile;
        this.description = description;
        this.unitPrice = unitPrice;
    }
}
