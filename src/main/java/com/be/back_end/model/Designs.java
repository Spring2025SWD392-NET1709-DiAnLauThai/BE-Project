package com.be.back_end.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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

    @Column(name="uploaddate",updatable = false)
    private LocalDateTime upload_date;
    @ManyToOne
    @JoinColumn(name = "accountid", nullable = false)  // Foreign key in the User table
    private Account account;

    @Column(name="designname")
    private String designName;

    @Column(name="designfile")
    private String designFile;

    @OneToMany(mappedBy = "design", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TshirtDesign> tshirtDesigns;

    private BigDecimal price;

    @OneToMany(mappedBy = "design",cascade = CascadeType.ALL)
    private Set<Orderitems> orderitems;

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        upload_date = LocalDateTime.now();

    }
}
