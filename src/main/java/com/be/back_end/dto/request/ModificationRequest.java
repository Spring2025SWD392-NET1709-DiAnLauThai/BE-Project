package com.be.back_end.dto.request;


import lombok.Data;

@Data
public class ModificationRequest {
    private String bookingdetailId;
    private String  modificationnote;
}
