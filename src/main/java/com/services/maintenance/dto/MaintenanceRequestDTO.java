package com.services.maintenance.dto;

import com.services.maintenance.enums.MaintenanceType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record MaintenanceRequestDTO(

        UUID vehicleId,

        LocalDate startDate,

        LocalDate endDate,

        MaintenanceType maintenanceType,

        Integer startKm,

        Integer endKm,

        String mechanicalWorkshop,

        BigDecimal cost,

        String observations

) {
}
