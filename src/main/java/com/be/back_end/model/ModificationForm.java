package com.be.back_end.model;

import com.be.back_end.enums.RequestStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="Modificationform")
@Getter
@Setter
@NoArgsConstructor
public class ModificationForm {
    @Id
    @Column(name="modificationid", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    private String id;

    @ManyToOne
    @JoinColumn(name="bookingdetailsid", nullable = false)
    private Bookingdetails bookingdetail;
    @Column(name="modificationnote", nullable = false)
    private String modificationnote;

    @Enumerated(EnumType.STRING)
    @Column(name="modificationstatus")
    private RequestStatusEnum requeststatus = RequestStatusEnum.PENDING;

    @Column(name="requestedat", nullable = false, updatable = false)
    private LocalDateTime requestedat;

    @Column(name="reviewedat")
    private LocalDateTime reviewedat;

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        requestedat = LocalDateTime.now();
    }
}
