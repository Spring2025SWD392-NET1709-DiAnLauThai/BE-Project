package com.be.back_end.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CreateDesignResponse {
    private String designId;
    private String designName;
    private String designFile;
    private BigDecimal price;
    private String uploadDate;

    private String tshirtId;
    private String tshirtName;
    private String tshirtDescription;
    private String tshirtStatus;
    private String tshirtImageUrl;
    private String tshirtCreatedAt;
}
