package com.be.back_end.model;


import com.be.back_end.enums.TshirtsEnums;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="Tshirts")
@Getter
@Setter
@NoArgsConstructor

public class Tshirts {

    @Id
    @Column(name="tshirtid",updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    private String id;

    @Column(name="tshirtname")
    private String name;

    @Column(name="tshirtdescription")
    private String description;

    @Enumerated(EnumType.STRING)
    private TshirtsEnums status;

    @Column(name="imageurl")
    private String image_url;

    @OneToMany(mappedBy = "tshirt", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TshirtDesign> tshirtDesigns;

    @OneToMany(mappedBy = "tshirt", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TShirtSize> tShirtSizes;

    @OneToMany(mappedBy = "tshirt", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TShirtColor> tShirtColors;

    @OneToMany(mappedBy = "tshirt", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TShirtMaterial> tshirtMaterials;

    private int quantity;

    @Column(name="createdat")
    private LocalDateTime created_at;

    @OneToMany(mappedBy = "tshirt",cascade = CascadeType.ALL)
    private Set<Orderitems> orderitems;

    @ManyToOne
    @JoinColumn(name="accountid",nullable = false)
    private Account account;
    @PrePersist
    protected  void OnCreate(){
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        created_at=LocalDateTime.now();
    }



}
