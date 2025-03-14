package com.be.back_end.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CancelBookingRequest {
    private String BookingId;
    private String note;
}
