package com.be.back_end.repository;


import com.be.back_end.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task,String> {
    Page<Task> findByBooking_startDateBetweenAndAccount_IdAndAccount_NameContainingIgnoreCaseAndTaskStatus(
            LocalDate startDate, LocalDate endDate, String designerId, String designerName, String taskStatus, Pageable pageable);

    Page<Task> findByBooking_startDateBetweenAndAccount_IdAndAccount_NameContainingIgnoreCase(
            LocalDate startDate, LocalDate endDate, String designerId, String designerName, Pageable pageable);

    Page<Task> findByBooking_startDateBetweenAndAccount_IdAndTaskStatus(
            LocalDate startDate, LocalDate endDate, String designerId, String taskStatus, Pageable pageable);

    Page<Task> findByAccount_IdAndAccount_NameContainingIgnoreCaseAndTaskStatus(
            String designerId, String designerName, String taskStatus, Pageable pageable);

    Page<Task> findByBooking_startDateBetweenAndAccount_Id(
            LocalDate startDate, LocalDate endDate, String designerId, Pageable pageable);

    Page<Task> findByAccount_IdAndAccount_NameContainingIgnoreCase(
            String designerId, String designerName, Pageable pageable);

    Page<Task> findByAccount_IdAndTaskStatus(String designerId, String taskStatus, Pageable pageable);

    Page<Task> findByAccount_Id(String designerId, Pageable pageable);
    Page<Task> findByTaskStatus(String taskStatus, Pageable pageable);
    Page<Task> findByAccount_NameContainingIgnoreCase(String designerName, Pageable pageable);
    Page<Task> findByAccount_NameContainingIgnoreCaseAndTaskStatus(
            String designerName, String taskStatus, Pageable pageable);
    Page<Task> findByBooking_startDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<Task> findByBooking_startDateBetweenAndTaskStatus(
            LocalDate startDate, LocalDate endDate, String taskStatus, Pageable pageable);
    Page<Task> findByBooking_startDateBetweenAndAccount_NameContainingIgnoreCase(
            LocalDate startDate, LocalDate endDate, String designerName, Pageable pageable);
    Page<Task> findByBooking_startDateBetweenAndAccount_NameContainingIgnoreCaseAndTaskStatus(
            LocalDate startDate, LocalDate endDate, String designerName, String taskStatus, Pageable pageable);
    Optional<Task> findByBookingId(String bookingId);


    List<Task> findByBookingIdIn(List<String> bookingIds);
}
