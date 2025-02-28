package com.be.back_end.dto.response;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TshirtsListsResponse {
    private String id;
    private String name;
    private String description;
    private String accountName;
    private String imageUrl;
    private LocalDateTime createdAt;
}
