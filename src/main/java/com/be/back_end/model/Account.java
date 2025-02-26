package com.be.back_end.model;



import com.be.back_end.enums.ActivationEnums;
import com.be.back_end.enums.RoleEnums;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="Account")
@Getter
@Setter
@NoArgsConstructor
public class Account {

    @Id

    @Column(name = "accountid", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    private String id;

    @Column(nullable = false, unique = true)
    private String email;

    private String name;
    private String password;
    private String address;
    private String phone;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivationEnums status;

    @Column(name = "dateofbirth")
    private LocalDate dateOfBirth;

    @Column(name = "createdat", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedat")
    private LocalDateTime updatedAt;


    @Enumerated(EnumType.STRING)
    private RoleEnums role;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private Set<Feedback> feedbacks;
    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    private Set<Designs> designs;
    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    private Set<Task> tasks;
    private String image_url;
    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    private Set<Bookings> bookings;
    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    
}
