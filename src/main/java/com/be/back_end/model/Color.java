package com.be.back_end.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "Color")
@Getter
@Setter
@NoArgsConstructor
public class Color {

    @Id
    @Column(name = "colorid", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    private String id;

    @Column(name = "color_name", nullable = false)
    private String colorName;

    @Column(name = "color_code", nullable = false, unique = true)
    private String colorCode;

    @OneToMany(mappedBy = "color", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TShirtColor> tShirtColors;
    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
}
