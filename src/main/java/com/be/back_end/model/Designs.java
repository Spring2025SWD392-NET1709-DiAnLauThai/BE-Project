package com.be.back_end.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="Designs")
@Getter
@Setter
@NoArgsConstructor

public class Designs {

    @Id
    @Column(name="designid", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    private String id;

    @ManyToOne
    @JoinColumn(name = "accountid", nullable = false)  // Foreign key in the User table. This is createdBy
    private Account account;


    @Column(name="designfile")
    private String designFile;

    @OneToMany(mappedBy = "design", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TshirtDesign> tshirtDesigns;

    @OneToMany(mappedBy = "design",cascade = CascadeType.ALL)
    private Set<Bookingdetails> bookingdetails;

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }


    }
}
