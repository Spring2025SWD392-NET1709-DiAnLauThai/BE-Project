package com.be.back_end.repository;


import com.be.back_end.enums.TaskStatusEnum;
import com.be.back_end.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface TasKRepository  extends JpaRepository<Task,String> {
    // Fetch tasks based on Booking start date between a range
    Page<Task> findByBooking_StartdateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    // Fetch tasks based on Booking status (taskStatus is stored in the Task entity, so we reference Booking here)
    Page<Task> findByBooking_Status(String bookingStatus, Pageable pageable);

    // Fetch tasks based on Account email from the Booking's account
    Page<Task> findByBooking_Account_EmailContainingIgnoreCase(String designerEmail, Pageable pageable);

    // Fetch tasks based on Account email and Booking status
    Page<Task> findByBooking_Account_EmailContainingIgnoreCaseAndBooking_Status(
            String designerEmail, String bookingStatus, Pageable pageable);

    // Fetch tasks based on Booking start date and status
    Page<Task> findByBooking_StartdateBetweenAndBooking_Status(
            LocalDate startDate, LocalDate endDate, String bookingStatus, Pageable pageable);

    // Fetch tasks based on Booking start date and Account email
    Page<Task> findByBooking_StartdateBetweenAndBooking_Account_EmailContainingIgnoreCase(
            LocalDate startDate, LocalDate endDate, String designerEmail, Pageable pageable);

    // Fetch tasks based on Booking start date, Account email, and Booking status
    Page<Task> findByBooking_StartdateBetweenAndBooking_Account_EmailContainingIgnoreCaseAndBooking_Status(
            LocalDate startDate, LocalDate endDate, String designerEmail, String bookingStatus, Pageable pageable);

}
