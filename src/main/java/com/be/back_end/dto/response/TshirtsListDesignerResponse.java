package com.be.back_end.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class TshirtsListDesignerResponse {
    private String tshirtId;
    private String name;
    private String description;
    private String imageFile;
    private String imageUrl;
    private LocalDateTime createdAt;

}
