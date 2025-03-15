package com.be.back_end.dto.request;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class UpdateBookingDetailsRequest {
    private String id;
    private String description;
}
