package com.be.back_end.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name="Payment")
@NoArgsConstructor
public class Payment {
    @Id
    @UuidGenerator
    @Column(name="paymentid", nullable = false,updatable = false)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "orderid", nullable = false)
    private Orders orders;

    @Column(name="paymentname")
    private String payment_name;
    @Column(name="paymentmethod")
    private String payment_method;
    @Column(name="paymentdate")
    private LocalDateTime payment_date;
    @Column(name="paymentamount")
    private BigDecimal payment_amount;
    @PrePersist
    protected void onCreate() {

        payment_date = LocalDateTime.now();
    }
}
