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
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    private String id;

    @ManyToOne
    @JoinColumn(name = "designid", nullable = false)
    private Designs design;

    @ManyToOne
    @JoinColumn(name = "tshirtid", nullable = false)
    private Tshirts tshirt;
    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
}
