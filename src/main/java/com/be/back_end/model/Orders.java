package com.be.back_end.model;


import com.be.back_end.enums.OrderEnums;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="Orders")
@Getter
@Setter
@NoArgsConstructor
public class Orders {
    @Id
    @Column(name="Orderid", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    private String Id;

    @Column(name="totalprice")
    private BigDecimal total_price;

    @Column(name="totalquantity")
    private int total_quantity;


    @ManyToOne
    @JoinColumn(name="accountid",nullable = false)
    private Account account;

    @OneToMany(mappedBy = "orders",cascade = CascadeType.ALL)
    private Set<Payment> payments;

    @Enumerated(EnumType.STRING)
    private OrderEnums status;

    @Column(name="datecreated")
    private LocalDateTime date_created;

    @Column(name="lastupdated")
    private LocalDateTime last_updated;

    @Column(name="ordernotes")
    private String order_notes;
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
