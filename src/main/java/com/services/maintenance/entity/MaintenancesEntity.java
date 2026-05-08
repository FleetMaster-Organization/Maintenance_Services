package com.services.maintenance.entity;

import com.services.maintenance.enums.MaintenanceType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenancesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private UUID idVehicle;

    private LocalDate startDate;

    private LocalDate endDate;

    private MaintenanceType maintenanceType;

    private Integer startKm;

    private Integer endKm;

    private String mechanicalWorkshop;

    private BigDecimal cost;

    private String observations;

    private String createdBy;

    private OffsetDateTime createdAt;
}
