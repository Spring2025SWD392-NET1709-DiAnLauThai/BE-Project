package com.be.back_end.model;


import com.be.back_end.enums.AccountEnums;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import javax.management.relation.Role;
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
    @UuidGenerator
    @Column(name = "Accountid", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    private String name;
    private String address;
    private String phone;
    private AccountEnums status;

    @Column(name = "Dateofbirth")
    private LocalDate dateOfBirth;

    @Column(name = "Createdat", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "Updatedat")
    private LocalDateTime updatedAt;

    private boolean isDeleted;
    @ManyToOne
    @JoinColumn(name = "roleid", nullable = false)  // Foreign key in the User table
    private Roles role;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private Set<Feedback> feedbacks;
    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
