package com.be.back_end.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TransactionResponse {
    public String code;
    public String message;
    public String paymentUrl;

}
