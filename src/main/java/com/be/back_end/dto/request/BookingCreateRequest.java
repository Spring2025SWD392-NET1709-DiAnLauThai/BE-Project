package com.be.back_end.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BookingCreateRequest {
    private String title;
    private LocalDateTime startdate;
    private LocalDateTime enddate;
    private List<BookingDetailCreateRequest> bookingdetails;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class BookingDetailCreateRequest {
        private String description;
        private String designFile;

        private BigDecimal unitprice;
    }
}


