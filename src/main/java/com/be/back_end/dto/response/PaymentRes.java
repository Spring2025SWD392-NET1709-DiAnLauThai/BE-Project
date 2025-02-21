package com.be.back_end.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PaymentRes implements Serializable {

    private String status;
    private String message;
    private String url;
}
