package com.services.maintenance.entity;

import com.services.maintenance.enums.MaintenanceType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@Table(name = "maintenances")
@NoArgsConstructor
@AllArgsConstructor
public class MaintenancesEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID vehicleId;

    private UUID scheduleId;

    private String vehiclePlate;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate nextScheduledDate;

    @Enumerated(EnumType.STRING)
    private MaintenanceType maintenanceType;

    private Double startKm;

    private Double endKm;

    private String mechanicalWorkshop;

    private BigDecimal cost;

    private String observations;

    private String createdBy;

    private OffsetDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = OffsetDateTime.now();
    }
}
