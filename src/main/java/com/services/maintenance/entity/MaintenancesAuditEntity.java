package com.services.maintenance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@Table(name = "maintenances_audit")
@NoArgsConstructor
@AllArgsConstructor
public class MaintenancesAuditEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID maintenanceId;

    private String actionType;

    private String modifiedField;

    private String oldValue;

    private String newValue;

    private String modifiedBy;

    private OffsetDateTime modifiedAt;
}
