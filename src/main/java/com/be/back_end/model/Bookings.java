package com.be.back_end.model;


import com.be.back_end.enums.BookingEnums;
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
    @Column(name="depositamount")
    private BigDecimal depositAmount;
    @Column(name="totalquantity")
    private int total_quantity;


    @ManyToOne
    @JoinColumn(name="accountid",nullable = false)
    private Account account;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Bookingdetails> bookingDetails;
    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private Transaction transaction;

    @Enumerated(EnumType.STRING)
    private BookingEnums status;

    @Column(name="datecreated", nullable = false, updatable = false)
    private LocalDateTime dateCreated;

    @Column(name="updateddate")
    private LocalDateTime dateUpdated;

    @Column(name="ispublic")
    private boolean isPublic;
    @Column(name="startdate")
    private LocalDateTime startDate;

    @Column(name="enddate")
    private LocalDateTime endDate;

    private String note;


    @Column(name = "code")
    private String code;

    @Column(name = "title")
    private String title;


    @PrePersist
    protected void onCreate() {
        if (Id == null) {
            Id = UUID.randomUUID().toString();
        }
        dateCreated = LocalDateTime.now();
        dateUpdated = dateCreated;
        isPublic =false;
    }
    @PreUpdate
    protected void onUpdate() {
        dateUpdated = LocalDateTime.now();
    }

}
