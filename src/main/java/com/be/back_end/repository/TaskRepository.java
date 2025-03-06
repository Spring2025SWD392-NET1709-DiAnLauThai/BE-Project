package com.be.back_end.repository;


import com.be.back_end.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface TaskRepository extends JpaRepository<Task,String> {
    Page<Task> findByTaskStatus(String taskStatus, Pageable pageable);
    Page<Task> findByAccount_NameContainingIgnoreCase(String designerName, Pageable pageable);
    Page<Task> findByAccount_NameContainingIgnoreCaseAndTaskStatus(
            String designerName, String taskStatus, Pageable pageable);
    Page<Task> findByBooking_StartdateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<Task> findByBooking_StartdateBetweenAndTaskStatus(
            LocalDate startDate, LocalDate endDate, String taskStatus, Pageable pageable);
    Page<Task> findByBooking_StartdateBetweenAndAccount_NameContainingIgnoreCase(
            LocalDate startDate, LocalDate endDate, String designerName, Pageable pageable);
    Page<Task> findByBooking_StartdateBetweenAndAccount_NameContainingIgnoreCaseAndTaskStatus(
            LocalDate startDate, LocalDate endDate, String designerName, String taskStatus, Pageable pageable);

}
