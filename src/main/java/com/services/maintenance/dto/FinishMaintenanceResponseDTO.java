package com.services.maintenance.dto;

import com.services.maintenance.enums.MaintenanceType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record FinishMaintenanceResponseDTO(

        UUID id,

        UUID vehicleId,

        String plate,

        LocalDate startDate,

        LocalDate endDate,

        LocalDate nextScheduledDate,

        MaintenanceType maintenanceType,

        Double startKm,

        Double endKm,

        String mechanicalWorkshop,

        BigDecimal cost,

        String observations,

        String createdBy,

        OffsetDateTime createdAt
) {
}
