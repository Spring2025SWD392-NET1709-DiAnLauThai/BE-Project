package com.be.back_end.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingResponseInDetailCus extends BookingResponseInDetail{
    private boolean fullyPaid;
}
