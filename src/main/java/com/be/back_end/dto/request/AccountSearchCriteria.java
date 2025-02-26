package com.be.back_end.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AccountSearchCriteria {
    private String keyword;

    @Min(0)
    private int page = 0;

    @Min(1)
    private int size = 10;

    private String filter;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime dateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime dateTo;

    private String sortDir = "asc";

    private String sortBy = "createdAt";
}
