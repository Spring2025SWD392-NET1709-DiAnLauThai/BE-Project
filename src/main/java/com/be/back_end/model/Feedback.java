package com.be.back_end.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Feedback")
public class Feedback {
    @Id
    @Column(name = "feedbackid", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    private String id;
    @Column(name="type")
    private String type;
    @Column(name="rating")
    private int rating;
    @Column(name="detail")
    private String detail;
    @Column(name = "createddate", updatable = false)
    private LocalDateTime Createddate;
    @ManyToOne
    @JoinColumn(name="accountid",nullable = false)
    private Account user;
    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        Createddate = LocalDateTime.now();

    }

}
