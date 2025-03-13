package com.be.back_end.model;

import com.be.back_end.enums.RequestStatusEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="Refundform")
@Getter
@Setter
@NoArgsConstructor
@Data
public class RefundForm {
    @Id
    @Column(name="refundid", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    private String id;

    @ManyToOne
    @JoinColumn(name = "bookingid", nullable = false)
    private Bookings booking;

    @Enumerated(EnumType.STRING)
    @Column(name="refundstatus")
    private RequestStatusEnum refundstatus = RequestStatusEnum.PENDING;

    @Column(name="refundamount", nullable = false)
    private BigDecimal refundamount;

    @Column(name="refundreason")
    private String refundreason;

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

