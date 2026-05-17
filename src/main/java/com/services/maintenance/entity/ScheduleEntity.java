package com.services.maintenance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@Table(name = "schedules")
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID vehicleId;

    private String vehiclePlate;

    private LocalDate scheduledDate;

    private String observations;

    private OffsetDateTime createdAt;

    private String createdBy;
}