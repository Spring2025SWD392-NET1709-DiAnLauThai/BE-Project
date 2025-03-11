package com.be.back_end.dto.request;

import com.be.back_end.model.Designs;
import com.be.back_end.model.Tshirts;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TshirtDesignDTO {
    private Designs design;
    private Tshirts tshirt;
}
