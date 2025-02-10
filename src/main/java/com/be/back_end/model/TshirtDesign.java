package com.be.back_end.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "Tshirtdesign")
@Getter
@Setter
@NoArgsConstructor
public class TshirtDesign {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "designid", nullable = false)  // Foreign Key to Designs
    private Designs design;

    @ManyToOne
    @JoinColumn(name = "tshirtid", nullable = false)  // Foreign Key to Tshirts
    private Tshirts tshirt;
}
