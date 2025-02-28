package com.be.back_end.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class TshirtsDTO {
    private String name;
    private String description;

    private String imageUrl;
    private LocalDateTime createdAt;
}
