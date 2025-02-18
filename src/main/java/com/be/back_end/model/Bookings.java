package com.be.back_end.model;


import com.be.back_end.enums.OrderEnums;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="Bookings")
@Getter
@Setter
@NoArgsConstructor
public class Bookings {
    @Id
    @Column(name="bookingid", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    private String Id;

    @Column(name="totalprice")
    private BigDecimal total_price;

    @Column(name="totalquantity")
    private int total_quantity;

    @OneToOne
    @JoinColumn(name="feedbackid", unique = true)
    private Feedback feedback;

    @ManyToOne
    @JoinColumn(name="accountid",nullable = false)
    private Account account;

    @OneToMany(mappedBy = "bookings",cascade = CascadeType.ALL)
    private Set<Transaction> transactions;
    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Task task;
    @Enumerated(EnumType.STRING)
    private OrderEnums status;

    @Column(name="datecreated")
    private LocalDateTime date_created;

    @Column(name="lastupdated")
    private LocalDateTime last_updated;


    @Column(name = "code")
    private String code;

    @Column(name = "title")
    private String title;

    @Column(name = "duration")
    private int duration;
    @PrePersist
    protected void onCreate() {
        if (Id == null) {
            Id = UUID.randomUUID().toString();
        }
        date_created = LocalDateTime.now();
        last_updated = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        last_updated = LocalDateTime.now();
    }

}
