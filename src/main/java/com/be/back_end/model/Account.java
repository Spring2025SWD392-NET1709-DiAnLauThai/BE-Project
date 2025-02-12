package com.be.back_end.model;


import com.be.back_end.enums.AccountEnums;
import com.be.back_end.enums.RoleEnums;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;


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
    private String address;
    private String phone;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountEnums status;

    @Column(name = "dateofbirth")
    private LocalDate dateOfBirth;

    @Column(name = "createdat", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedat")
    private LocalDateTime updatedAt;

    private boolean isdeleted;

    @Enumerated(EnumType.STRING)
    private RoleEnums role;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private Set<Feedback> feedbacks;
    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    private Set<Designs> designs;
    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    private Set<Tshirts> tshirts;
    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    private Set<Orders> orders;
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
