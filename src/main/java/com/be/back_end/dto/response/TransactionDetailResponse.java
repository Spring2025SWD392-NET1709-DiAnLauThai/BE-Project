package com.be.back_end.dto.response;

import com.be.back_end.dto.TransactionDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class TransactionDetailResponse {

    private TransactionDTO transaction;
    private List<BookingDetailResponseDTO> bookingDetail;

}
