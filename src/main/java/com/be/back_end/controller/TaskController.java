package com.be.back_end.controller;

import com.be.back_end.dto.request.TshirtSelectRequest;
import com.be.back_end.dto.response.*;
import com.be.back_end.enums.TaskStatusEnum;
import com.be.back_end.service.TaskService.ITaskService;
import com.be.back_end.dto.request.TaskCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    @GetMapping("/detail/{taskId}")
    public ResponseEntity<?> getTaskDetailByTaskId(@PathVariable String taskId) {
        try {
            TaskDetailResponseDTO taskDetailResponseDTO = taskService.getTaskDetailByTaskId(taskId);
            if (taskDetailResponseDTO == null) {
                return ResponseEntity.status(404).body(
                        new ErrorResponse(404, "Task not found", List.of("No task found with the provided ID."))
                );
            }
            return ResponseEntity.ok(new ApiResponse<>(200, taskDetailResponseDTO, "Task detail retrieved successfully."));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse(500, "Unexpected error occurred", List.of(e.getMessage()))
            );
        }
    }
    @PutMapping("/select/tshirt")
    public ResponseEntity<?> assignTask(@RequestBody TshirtSelectRequest tshirtSelectRequest) {
        try {
            boolean isAssigned = taskService.assignTshirttoTask(tshirtSelectRequest);
            if (isAssigned) {
                return ResponseEntity.ok(new ApiResponse<>(200, List.of("Tshirt added to booking detail."), "Success"));
            } else {
                return ResponseEntity.badRequest().body(
                        new ErrorResponse(400, "Failed to assign task",
                                List.of("Invalid bookingdetail or tshirt."))
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
