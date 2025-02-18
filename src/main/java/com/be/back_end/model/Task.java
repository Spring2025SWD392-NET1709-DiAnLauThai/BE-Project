package com.be.back_end.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "task")  // Matches the ERD table name
@Getter
@Setter
@NoArgsConstructor
public class Task {

    @Id
    @Column(name = "taskid", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    private String id;

    @ManyToOne
    @JoinColumn(name = "designerid", nullable = false)
    private Account account;

    @OneToOne
    @JoinColumn(name = "bookingid", nullable = false)
    private Bookings booking;


    @Column(name = "taskstatus", nullable = false)
    private String taskStatus;

    @Column(name = "startdate", nullable = false)
    private LocalDate startDate;

    @Column(name = "enddate", nullable = false)
    private LocalDate endDate;

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
}
