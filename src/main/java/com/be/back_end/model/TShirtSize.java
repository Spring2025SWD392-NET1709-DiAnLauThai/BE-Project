package com.be.back_end.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "TShirtsize")
@Getter
@Setter
@NoArgsConstructor
public class TShirtSize {

    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    private String id;

    @ManyToOne
    @JoinColumn(name = "tshirt_id", nullable = false)
    private Tshirts tshirt;

    @ManyToOne
    @JoinColumn(name = "size_id", nullable = false)
    private Size size;
    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
}
