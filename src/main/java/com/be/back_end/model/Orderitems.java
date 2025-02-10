package com.be.back_end.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name="Orderitems")
@Getter
@Setter
@NoArgsConstructor
public class Orderitems {
    @Id
    @UuidGenerator
    @Column(name="Orderitemsid", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name="tshirtid",nullable = false)
    private Tshirts tshirt;

    @ManyToOne
    @JoinColumn(name="designid",nullable = false)
    private Designs design;


    private int quantity;
    @Column(name="unitprice")
    private BigDecimal unit_price;
}
