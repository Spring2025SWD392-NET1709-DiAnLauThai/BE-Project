package com.be.back_end.service.TaskService;

import com.be.back_end.dto.request.TaskCreateRequest;
import com.be.back_end.dto.request.TshirtSelectRequest;
import com.be.back_end.dto.response.PaginatedResponseDTO;
import com.be.back_end.dto.response.TaskDetailResponseDTO;
import com.be.back_end.dto.response.TaskListResponse;

import java.time.LocalDate;

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
    PaginatedResponseDTO<TaskListResponse> getAllTaskForCurrentDesigner(
            LocalDate startDate,
            LocalDate endDate,
            String designerName,
            String taskStatus,
            int page,
            int size,
            String sortDir);

    boolean assignTshirttoTask(TshirtSelectRequest tshirtSelectRequest);
    TaskDetailResponseDTO getTaskDetailByTaskId(String taskId);
    boolean confirmCompletion(String bookingId);
}
