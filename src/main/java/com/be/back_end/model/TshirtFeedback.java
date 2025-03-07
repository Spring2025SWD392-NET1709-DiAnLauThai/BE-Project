package com.be.back_end.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "Tshirtfeedback")
@Getter
@Setter
@NoArgsConstructor
public class TshirtFeedback {
    @Id
    @Column(name="tshirtfeedbackid",updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    private String id;

    @ManyToOne
    @JoinColumn(name = "tshirtid", nullable = false)
    private Tshirts tshirt;

    @ManyToOne
    @JoinColumn(name = "feedbackid", nullable = false)
    private Feedback feedback;
    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }

    }
}
