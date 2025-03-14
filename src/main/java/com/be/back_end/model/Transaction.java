package com.be.back_end.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name="Transaction")
@NoArgsConstructor
public class Transaction {
    @Id
    @Column(name="transactionid", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    private String id;
    @OneToOne
    @JoinColumn(name = "bookingid", nullable = false, unique = true)
    private Bookings booking;

    @Column(name="transactionname", nullable = false)
    private String transactionName;

    @Column(name="transactionmethod", nullable = false)
    private String transactionMethod;

    @Column(name="transactiondate", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name="transactionamount", nullable = false)
    private BigDecimal transactionAmount;

    @Column(name="transactionstatus", nullable = false)
    private String transactionStatus;

    @Column(name="bankcode")
    private String bankCode;


    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        transactionDate = LocalDateTime.now();
    }
}
