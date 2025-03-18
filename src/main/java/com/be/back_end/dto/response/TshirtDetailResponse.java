package com.be.back_end.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class TshirtDetailResponse {

    private String description;
    private String image_url;
    private String tshirtName;
    private LocalDateTime createdAt;
    private List<ColorResponse> colors;
    @Getter
    @Setter
    public static class ColorResponse {
        private String colorId;
        private String colorName;
        private String colorCode;
    }
}
