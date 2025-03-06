package com.be.back_end.dto.response;

import com.be.back_end.model.Account;
import com.be.back_end.model.Bookings;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TaskListResponse {

    private String taskId;

    private String designerName;

    private String taskStatus;

    private String bookingId;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

}
