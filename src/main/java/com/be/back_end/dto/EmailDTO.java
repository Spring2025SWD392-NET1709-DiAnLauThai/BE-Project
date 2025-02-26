package com.be.back_end.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailDTO {
    private String to;
    private String name;
    private String subject;
    private String template;
    private Object data;
}
