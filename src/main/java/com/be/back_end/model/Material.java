package com.be.back_end.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="Material")
public class Material {

    @Id
    @UuidGenerator
    @Column(name="materialid",nullable = false)
    private UUID id;

    private String name;

    private BigDecimal price;

    private int quantity;

    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TShirtMaterial> tshirtMaterials;
}
