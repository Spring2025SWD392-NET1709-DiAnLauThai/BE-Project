package com.be.back_end.model;


import com.be.back_end.enums.ActivationEnums;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private ActivationEnums status;

    @Column(name="imageurl")
    private String image_url;

    @Column(name="imagesfile")
    private String imagesfile;

    @OneToMany(mappedBy = "tshirt", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TshirtDesign> tshirtDesigns;



    @OneToMany(mappedBy = "tshirt", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TShirtColor> tShirtColors;

    @OneToMany(mappedBy = "tshirt", cascade = CascadeType.ALL)
    private Set<TshirtFeedback> tshirtFeedbacks;

    @Column(name="createdat")
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "tshirt", cascade = CascadeType.ALL, orphanRemoval = true)
    private Bookingdetails bookingdetails;



    @PrePersist
    protected  void OnCreate(){
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        createdAt=LocalDateTime.now();
    }



}
