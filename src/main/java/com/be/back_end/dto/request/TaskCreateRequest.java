package com.be.back_end.dto.request;

import com.be.back_end.model.Account;
import com.be.back_end.model.Bookings;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskCreateRequest {
    private String bookingId;
    private String designerId;

}
