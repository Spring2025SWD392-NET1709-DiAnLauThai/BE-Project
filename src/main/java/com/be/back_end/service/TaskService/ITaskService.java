package com.be.back_end.service.TaskService;

import com.be.back_end.dto.TshirtsDTO;
import com.be.back_end.dto.request.TaskCreateRequest;
import com.be.back_end.dto.response.PaginatedResponseDTO;
import com.be.back_end.dto.response.TaskListResponse;
import com.be.back_end.enums.TaskStatusEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ITaskService {
    boolean assignTask(TaskCreateRequest taskCreateRequest);

    PaginatedResponseDTO<TaskListResponse> getAllTask(
            LocalDate startDate,
            LocalDate endDate,
            String designerEmail,
            String  taskStatus,
            int page,
            int size,
            String sortDir);
}
