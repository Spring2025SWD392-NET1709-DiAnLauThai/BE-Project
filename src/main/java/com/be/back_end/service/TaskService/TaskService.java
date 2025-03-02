package com.be.back_end.service.TaskService;

import com.be.back_end.dto.TshirtsDTO;
import com.be.back_end.dto.request.TaskCreateRequest;
import com.be.back_end.dto.response.PaginatedResponseDTO;
import com.be.back_end.dto.response.TaskListResponse;
import com.be.back_end.enums.TaskStatusEnum;
import com.be.back_end.model.Account;
import com.be.back_end.model.Bookings;
import com.be.back_end.model.Task;
import com.be.back_end.model.Tshirts;
import com.be.back_end.repository.AccountRepository;
import com.be.back_end.repository.BookingRepository;
import com.be.back_end.repository.TasKRepository;
import com.be.back_end.service.EmailService.IEmailService;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService implements ITaskService{
        private final TasKRepository taskRepository;
        private final BookingRepository bookingRepository;
        private final AccountRepository accountRepository;
        private final IEmailService emailService;
    public TaskService(TasKRepository tasKRepository, BookingRepository bookingRepository, AccountRepository accountRepository, IEmailService emailService) {
        this.taskRepository = tasKRepository;
        this.bookingRepository = bookingRepository;
        this.accountRepository = accountRepository;
        this.emailService = emailService;
    }


    @Override
    public PaginatedResponseDTO<TaskListResponse> getAllTask(
            LocalDate startDate,
            LocalDate endDate,
            String designerEmail,
            String  taskStatus,
            int page,
            int size,
            String sortDir) {
        Sort.Direction sort;
        if(sortDir.equals("asc")){
            sort=Sort.Direction.ASC;
        }else{
            sort= Sort.Direction.DESC;}

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sort, "startDate"));
        Page<Task> tasks;
        if (startDate != null && endDate != null && designerEmail != null && taskStatus != null) {
            tasks = taskRepository.findByStartDateBetweenAndAccount_EmailContainingIgnoreCaseAndTaskStatus(
                    startDate, endDate, designerEmail, taskStatus, pageable);
        } else if (startDate != null && endDate != null && designerEmail != null) {
            tasks = taskRepository.findByStartDateBetweenAndAccount_EmailContainingIgnoreCase(
                    startDate, endDate, designerEmail, pageable);
        } else if (startDate != null && endDate != null && taskStatus != null) {
            tasks = taskRepository.findByStartDateBetweenAndTaskStatus(startDate, endDate, taskStatus, pageable);
        } else if (designerEmail != null && taskStatus != null) {
            tasks = taskRepository.findByAccount_EmailContainingIgnoreCaseAndTaskStatus(designerEmail, taskStatus, pageable);
        } else if (startDate != null && endDate != null) {
            tasks = taskRepository.findByStartDateBetween(startDate, endDate, pageable);
        } else if (designerEmail != null) {
            tasks = taskRepository.findByAccount_EmailContainingIgnoreCase(designerEmail, pageable);
        } else if (taskStatus != null) {
            tasks = taskRepository.findByTaskStatus(taskStatus, pageable);
        } else {
            tasks = taskRepository.findAll(pageable);
        }

        List<TaskListResponse> taskListResponses= new ArrayList<>();
        for(Task task:tasks.getContent()){
            taskListResponses.add(mapToTaskListResponse(task));
        }
        PaginatedResponseDTO<TaskListResponse>response =new PaginatedResponseDTO<>();
        response.setContent(taskListResponses);
        response.setPageSize(tasks.getSize());
        response.setPageNumber(tasks.getNumber());
        response.setTotalPages(tasks.getTotalPages());
        response.setTotalElements(tasks.getTotalElements());
        return  response;


    }
    private TaskListResponse mapToTaskListResponse(Task task) {
        TaskListResponse response = new TaskListResponse();
        response.setId(task.getId());
        response.setDesigner_name(task.getAccount().getEmail());
        response.setBooking(task.getBooking().getCode());
        response.setTaskStatus(task.getTaskStatus());
        response.setStartDate(task.getStartDate());
        response.setEndDate(task.getEndDate());
        return response;
    }
    @Override
    public boolean assignTask(TaskCreateRequest taskCreateRequest) {
        if (!bookingRepository.existsById(taskCreateRequest.getBookingId())
                || !accountRepository.existsById(taskCreateRequest.getDesignerId())) {
            return false;
        }
        Bookings bookings = bookingRepository.findById(taskCreateRequest.getBookingId()).orElse(null);
        Account designer = accountRepository.findById(taskCreateRequest.getDesignerId()).orElse(null);
        if (bookings == null || designer == null) {
            return false;
        }
        Task task = new Task();
        task.setTaskStatus(TaskStatusEnum.ASSIGNED.toString());
        task.setStartDate(taskCreateRequest.getStartDate());
        task.setEndDate(taskCreateRequest.getEndDate());
        task.setBooking(bookings);
        task.setAccount(designer);
        taskRepository.save(task);
        try {
            emailService.sendAssignmentEmail(designer.getEmail(), designer.getName(), bookings.getCode());
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
