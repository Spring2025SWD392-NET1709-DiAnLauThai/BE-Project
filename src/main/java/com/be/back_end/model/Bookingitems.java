package com.be.back_end.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name="Orderitems")
@Getter
@Setter
@NoArgsConstructor
public class Bookingitems {
    @Id
    @Column(name="Orderitemsid", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    private String id;

    @ManyToOne
    @JoinColumn(name="tshirtid",nullable = false)
    private Tshirts tshirt;

    @ManyToOne
    @JoinColumn(name="designid",nullable = false)
    private Designs design;


    private int quantity;
    @Column(name="unitprice")
    private BigDecimal unit_price;
    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
}
