package com.be.back_end.service.TaskService;

import com.be.back_end.dto.request.TaskCreateRequest;
import com.be.back_end.dto.request.TshirtSelectRequest;
import com.be.back_end.dto.response.BookingResponseNoLinkDTO;
import com.be.back_end.dto.response.PaginatedResponseDTO;
import com.be.back_end.dto.response.TaskDetailResponseDTO;
import com.be.back_end.dto.response.TaskListResponse;
import com.be.back_end.enums.BookingEnums;
import com.be.back_end.enums.TaskStatusEnum;
import com.be.back_end.model.*;
import com.be.back_end.repository.*;
import com.be.back_end.service.EmailService.IEmailService;
import com.be.back_end.utils.AccountUtils;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService implements ITaskService{
        private final TaskRepository taskRepository;
        private final BookingRepository bookingRepository;
        private final AccountRepository accountRepository;
        private final IEmailService emailService;
        private final AccountUtils accountUtils;
        private final BookingDetailsRepository bookingDetailsRepository;
        private final TshirtsRepository tshirtsRepository;
    public TaskService(TaskRepository tasKRepository, BookingRepository bookingRepository, AccountRepository accountRepository, IEmailService emailService, AccountUtils accountUtils, BookingDetailsRepository bookingDetailsRepository, TshirtsRepository tshirtsRepository) {
        this.taskRepository = tasKRepository;
        this.bookingRepository = bookingRepository;
        this.accountRepository = accountRepository;
        this.emailService = emailService;
        this.accountUtils = accountUtils;
        this.bookingDetailsRepository = bookingDetailsRepository;

        this.tshirtsRepository = tshirtsRepository;
    }


    @Transactional(readOnly = true)
    @Override
    public PaginatedResponseDTO<TaskListResponse> getAllTaskForCurrentDesigner(
            LocalDate startDate,
            LocalDate endDate,
            String designerName,
            String taskStatus,
            int page,
            int size,
            String sortDir) {
        Account currentAccount = accountUtils.getCurrentAccount();
        String designerId = currentAccount.getId();
        Sort.Direction sort = sortDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sort, "booking.startdate"));
        Page<Task> tasks;
        if (startDate != null && endDate != null && designerName != null && taskStatus != null) {
            tasks = taskRepository.findByBooking_StartdateBetweenAndAccount_IdAndAccount_NameContainingIgnoreCaseAndTaskStatus(
                    startDate, endDate, designerId, designerName, taskStatus, pageable);
        } else if (startDate != null && endDate != null && designerName != null) {
            tasks = taskRepository.findByBooking_StartdateBetweenAndAccount_IdAndAccount_NameContainingIgnoreCase(
                    startDate, endDate, designerId, designerName, pageable);
        } else if (startDate != null && endDate != null && taskStatus != null) {
            tasks = taskRepository.findByBooking_StartdateBetweenAndAccount_IdAndTaskStatus(
                    startDate, endDate, designerId, taskStatus, pageable);
        } else if (designerName != null && taskStatus != null) {
            tasks = taskRepository.findByAccount_IdAndAccount_NameContainingIgnoreCaseAndTaskStatus(
                    designerId, designerName, taskStatus, pageable);
        } else if (startDate != null && endDate != null) {
            tasks = taskRepository.findByBooking_StartdateBetweenAndAccount_Id(startDate, endDate, designerId, pageable);
        } else if (designerName != null) {
            tasks = taskRepository.findByAccount_IdAndAccount_NameContainingIgnoreCase(designerId, designerName, pageable);
        } else if (taskStatus != null) {
            tasks = taskRepository.findByAccount_IdAndTaskStatus(designerId, taskStatus, pageable);
        } else {
            tasks = taskRepository.findByAccount_Id(designerId, pageable); // Fetch only tasks belonging to the designer
        }

        List<TaskListResponse> taskListResponses = new ArrayList<>();
        for (Task task : tasks.getContent()) {
            taskListResponses.add(mapToTaskListResponse(task));
        }

        PaginatedResponseDTO<TaskListResponse> response = new PaginatedResponseDTO<>();
        response.setContent(taskListResponses);
        response.setPageSize(tasks.getSize());
        response.setPageNumber(tasks.getNumber());
        response.setTotalPages(tasks.getTotalPages());
        response.setTotalElements(tasks.getTotalElements());

        return response;
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedResponseDTO<TaskListResponse> getAllTask(
            LocalDate startDate,
            LocalDate endDate,
            String designerName,
            String taskStatus,
            int page,
            int size,
            String sortDir) {

        Sort.Direction sort = sortDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sort, "booking.startdate"));
        Page<Task> tasks;
        if (startDate != null && endDate != null && designerName != null && taskStatus != null) {
            tasks = taskRepository.findByBooking_StartdateBetweenAndAccount_NameContainingIgnoreCaseAndTaskStatus(
                    startDate, endDate, designerName, taskStatus, pageable);
        } else if (startDate != null && endDate != null && designerName != null) {
            tasks = taskRepository.findByBooking_StartdateBetweenAndAccount_NameContainingIgnoreCase(
                    startDate, endDate, designerName, pageable);
        } else if (startDate != null && endDate != null && taskStatus != null) {
            tasks = taskRepository.findByBooking_StartdateBetweenAndTaskStatus(
                    startDate, endDate, taskStatus, pageable);
        } else if (designerName != null && taskStatus != null) {
            tasks = taskRepository.findByAccount_NameContainingIgnoreCaseAndTaskStatus(
                    designerName, taskStatus, pageable);
        } else if (startDate != null && endDate != null) {
            tasks = taskRepository.findByBooking_StartdateBetween(startDate, endDate, pageable);
        } else if (designerName != null) {
            tasks = taskRepository.findByAccount_NameContainingIgnoreCase(designerName, pageable);
        } else if (taskStatus != null) {
            tasks = taskRepository.findByTaskStatus(taskStatus, pageable);
        } else {
            tasks = taskRepository.findAll(pageable);
        }
        List<TaskListResponse> taskListResponses = new ArrayList<>();
        for (Task task : tasks.getContent()) {
            taskListResponses.add(mapToTaskListResponse(task));
        }
        PaginatedResponseDTO<TaskListResponse> response = new PaginatedResponseDTO<>();
        response.setContent(taskListResponses);
        response.setPageSize(tasks.getSize());
        response.setPageNumber(tasks.getNumber());
        response.setTotalPages(tasks.getTotalPages());
        response.setTotalElements(tasks.getTotalElements());
        return response;
    }

    private TaskListResponse mapToTaskListResponse(Task task) {
        TaskListResponse response = new TaskListResponse();
        response.setTaskId(task.getId());
        response.setDesignerName(task.getAccount().getName());
        response.setTaskStatus(task.getTaskStatus());
        response.setStartDate(task.getBooking().getStartdate());
        response.setEndDate(task.getBooking().getEnddate());
        response.setBookingId(task.getBooking().getId());
        return response;
    }
    @Override
    public boolean assignTask(TaskCreateRequest taskCreateRequest) {
        Bookings bookings = bookingRepository.findById(taskCreateRequest.getBookingId()).orElse(null);
        Account designer = accountRepository.findById(taskCreateRequest.getDesignerId()).orElse(null);

        if (bookings == null || designer == null) {
            return false;
        }

        Task task = new Task();
        task.setTaskStatus(TaskStatusEnum.ASSIGNED.toString());
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
    @Override
    public boolean assignTshirttoTask(TshirtSelectRequest tshirtSelectRequest) {
        Bookingdetails bookingdetails = bookingDetailsRepository.findById(tshirtSelectRequest.getBookingDetailId())
                .orElse(null);
        Tshirts tshirt = tshirtsRepository.findById(tshirtSelectRequest.getTshirtId())
                .orElse(null);
        if (bookingdetails.getTshirt() != null && bookingdetails.getTshirt().getId().equals(tshirtSelectRequest.getTshirtId())) {
            return false;
        }


        if (bookingdetails == null || tshirt == null) {
            return false;
        }
        Bookings booking = bookingdetails.getBooking();
        booking.setUpdateddate(LocalDateTime.now());
        if (booking != null && booking.getStatus() == BookingEnums.COMPLETED) {
            return false;
        }

        bookingdetails.setTshirt(tshirt);
        bookingRepository.save(booking);
        bookingDetailsRepository.save(bookingdetails);
        return true;
    }

    @Override
    public boolean confirmCompletion(String bookingId) {
        Bookings booking = bookingRepository.findById(bookingId).orElse(null);
        Task task = taskRepository.findByBookingId(bookingId).orElse(null);
        if (booking == null || task == null) {
            return false;
        }
        boolean hasIncompleteDetails = bookingDetailsRepository
                .existsByBookingIdAndTshirtIsNull(bookingId);
        if (hasIncompleteDetails) {
            return false;
        }
        String customerEmail = booking.getAccount().getEmail();
        String customerName = booking.getAccount().getName();
        String bookingCode = booking.getCode();
        try {
            emailService.sendCustomerCompleteEmail(customerEmail, customerName, bookingCode);
        } catch (RuntimeException e) {
            return false;
        }
        task.setTaskStatus(TaskStatusEnum.COMPLETE.toString());
        booking.setStatus(BookingEnums.COMPLETED);
        taskRepository.save(task);
        bookingRepository.save(booking);
        return true;
    }
    @Transactional(readOnly = true)
    @Override
    public TaskDetailResponseDTO getTaskDetailByTaskId(String taskId) {
        Task task = getTask(taskId);
        Bookings booking = getBooking(task);
        List<Bookingdetails> bookingDetails = getBookingDetails(task);
        return buildTaskDetailResponseDTO(task, booking, bookingDetails);
    }
    private Task getTask(String taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + taskId));
    }
    private Bookings getBooking(Task task) {
        return bookingRepository.findById(task.getBooking().getId())
                .orElseThrow(() -> new EntityNotFoundException("Booking not found for Task ID: " + task.getId()));
    }
    private List<Bookingdetails> getBookingDetails(Task task) {
        return bookingDetailsRepository.findByBookingId(task.getBooking().getId());
    }
    private TaskDetailResponseDTO buildTaskDetailResponseDTO(Task task, Bookings booking, List<Bookingdetails> bookingDetails) {
        TaskDetailResponseDTO taskDetailResponseDTO = new TaskDetailResponseDTO();
        taskDetailResponseDTO.setDesignerName(task.getAccount().getName());
        taskDetailResponseDTO.setCode(booking.getCode());
        taskDetailResponseDTO.setTitle(booking.getTitle());
        taskDetailResponseDTO.setBookingStatus(booking.getStatus());
        taskDetailResponseDTO.setEnddate(booking.getEnddate());
        taskDetailResponseDTO.setTotalQuantity(booking.getTotal_quantity());
        taskDetailResponseDTO.setTotalPrice(booking.getTotal_price());
        taskDetailResponseDTO.setStartdate(booking.getStartdate());
        taskDetailResponseDTO.setUpdateddate(booking.getUpdateddate());
        taskDetailResponseDTO.setDatecreated(booking.getDatecreated());
        List<TaskDetailResponseDTO.BookingDetailResponse> detailResponses = bookingDetails.stream()
                .map(detail -> new TaskDetailResponseDTO.BookingDetailResponse(
                        detail.getId(),
                        detail.getDescription(),
                        detail.getUnit_price(),
                        new TaskDetailResponseDTO.DesignResponse(
                                detail.getDesign().getDesignFile()
                        ),
                        detail.getTshirt() != null ? new TaskDetailResponseDTO.TShirtResponse(
                                detail.getTshirt().getName(),
                                detail.getTshirt().getDescription(),
                                detail.getTshirt().getImage_url(),
                                detail.getTshirt().getTShirtColors().stream()
                                        .map(color -> new TaskDetailResponseDTO.TShirtColorResponse(
                                                color.getColor().getColorName(),
                                                color.getColor().getColorCode()))
                                        .toList()
                        ) : null
                ))
                .toList();
        taskDetailResponseDTO.setBookingDetails(detailResponses);
        return taskDetailResponseDTO;
    }


}
