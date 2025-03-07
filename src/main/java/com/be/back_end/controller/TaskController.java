package com.be.back_end.controller;

import com.be.back_end.dto.TshirtsDTO;
import com.be.back_end.dto.response.ApiResponse;
import com.be.back_end.dto.response.ErrorResponse;
import com.be.back_end.dto.response.PaginatedResponseDTO;
import com.be.back_end.dto.response.TaskListResponse;
import com.be.back_end.enums.TaskStatusEnum;
import com.be.back_end.service.TaskService.ITaskService;
import com.be.back_end.dto.request.TaskCreateRequest;
import jakarta.mail.MessagingException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    private final ITaskService taskService;

    public TaskController(ITaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<?> getAllTask( @RequestParam(required = false) LocalDate startDate,
                                           @RequestParam(required = false) LocalDate endDate,
                                           @RequestParam(required = false) String designerName,
                                           @RequestParam(required = false) TaskStatusEnum taskStatus,
                                           @RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "asc") String sortDir) {
        if (page <1 || size <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponse(400, null, List.of("Page and size must be positive values")));
        }
        String taskStatusStr = (taskStatus != null) ? taskStatus.toString() : null;

        PaginatedResponseDTO<TaskListResponse> tasks = taskService.getAllTask(
                startDate, endDate, designerName, taskStatusStr , page, size, sortDir);
        if (tasks.getContent().isEmpty()) {
            return ResponseEntity.ok(new ApiResponse<>(200, tasks, "No data available"));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, tasks, "Page returned: " + page));
    }
    @GetMapping("/designer")
    public ResponseEntity<?> getAllTaskForDesigner( @RequestParam(required = false) LocalDate startDate,
                                         @RequestParam(required = false) LocalDate endDate,
                                         @RequestParam(required = false) String designerName,
                                         @RequestParam(required = false) TaskStatusEnum taskStatus,
                                         @RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(defaultValue = "asc") String sortDir) {
        if (page <1 || size <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponse(400, null, List.of("Page and size must be positive values")));
        }
        String taskStatusStr = (taskStatus != null) ? taskStatus.toString() : null;

        PaginatedResponseDTO<TaskListResponse> tasks = taskService.getAllTaskForCurrentDesigner(
                startDate, endDate, designerName, taskStatusStr , page, size, sortDir);
        if (tasks.getContent().isEmpty()) {
            return ResponseEntity.ok(new ApiResponse<>(200, tasks, "No data available"));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, tasks, "Page returned: " + page));
    }


    @PostMapping("/assign")
    public ResponseEntity<?> assignTask(@RequestBody TaskCreateRequest taskCreateRequest) {
        try {
            boolean isAssigned = taskService.assignTask(taskCreateRequest);

            if (isAssigned) {
                return ResponseEntity.ok(new ApiResponse<>(200, List.of("Task assigned successfully."), "Success"));
            } else {
                return ResponseEntity.badRequest().body(
                        new ErrorResponse(400, "Failed to assign task",
                                List.of("Invalid booking ID or designer ID."))
                );
            }

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse(500, "Unexpected error occurred",
                            List.of(e.getMessage()))
            );
        }
    }
}
