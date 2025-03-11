package com.be.back_end.dto.request;


import com.be.back_end.model.Color;
import lombok.Data;

import java.util.List;

@Data
public class TshirtCreateRequest {
    private String bookingDetailId;
    private String description;
    private String imgurl;
    private String tshirtname;
    private List<String> colorlist;

}
