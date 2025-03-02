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
    Page<Task> findByStartDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<Task> findByTaskStatus(String taskStatus, Pageable pageable);

    Page<Task> findByAccount_EmailContainingIgnoreCase(String designerEmail, Pageable pageable);

    Page<Task> findByAccount_EmailContainingIgnoreCaseAndTaskStatus(String designerEmail, String taskStatus, Pageable pageable);

    Page<Task> findByStartDateBetweenAndTaskStatus(LocalDate startDate, LocalDate endDate, String taskStatus, Pageable pageable);

    Page<Task> findByStartDateBetweenAndAccount_EmailContainingIgnoreCase(
            LocalDate startDate, LocalDate endDate, String designerEmail, Pageable pageable);

    Page<Task> findByStartDateBetweenAndAccount_EmailContainingIgnoreCaseAndTaskStatus(
            LocalDate startDate, LocalDate endDate, String designerEmail, String taskStatus, Pageable pageable);

}
