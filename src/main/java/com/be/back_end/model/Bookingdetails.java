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

    @OneToMany(mappedBy = "bookingdetail", cascade = CascadeType.ALL)
    private Set<ModificationRequest> modificationRequests;

    @ManyToOne
    @JoinColumn(name="tshirtid")
    private Tshirts tshirt;

    @ManyToOne
    @JoinColumn(name="bookingid", nullable = false)
    private Bookings booking;

    @ManyToOne
    @JoinColumn(name="designid",nullable = false)
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
