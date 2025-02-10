package com.be.back_end.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "TShirtcolor")
@Getter
@Setter
@NoArgsConstructor
public class TShirtColor {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "tshirt_id", nullable = false)
    private Tshirts tshirt;

    @ManyToOne
    @JoinColumn(name = "color_id", nullable = false)
    private Color color;
}
