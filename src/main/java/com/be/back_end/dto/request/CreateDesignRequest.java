package com.be.back_end.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
public class CreateDesignRequest {


    private String description;
    private String designName;
    private MultipartFile designFile;
    private BigDecimal price;
    private String colorid;
}
