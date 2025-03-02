package com.be.back_end.dto.response;

import com.be.back_end.model.Account;
import com.be.back_end.model.Bookings;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskListResponse {

    private String id;

    private String designer_name;

    private String booking;

    private String taskStatus;

    private LocalDate startDate;

    private LocalDate endDate;

}
