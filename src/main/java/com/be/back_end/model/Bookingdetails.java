package com.be.back_end.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="Bookingdetails")
@Getter
@Setter
@NoArgsConstructor
public class Bookingdetails {
    @Id
    @Column(name="bookingdetailsid", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    private String id;

    @OneToOne
    @JoinColumn(name = "tshirtid", unique = true)
    private Tshirts tshirt;

    @ManyToOne
    @JoinColumn(name="bookingid", nullable = false)
    private Bookings booking;

    @OneToOne
    @JoinColumn(name = "designid", unique = true)
    private Designs design;


    private String description;

    @Column(name="unitprice")
    private BigDecimal unit_price;
    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
}
