package com.be.back_end.dto.response;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TshirtsListAvailableResponse{
    private String tshirtId;
    private String name;
    private String description;
    private String imageUrl;
}
