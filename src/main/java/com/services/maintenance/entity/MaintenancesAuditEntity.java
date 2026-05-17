package com.services.maintenance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
