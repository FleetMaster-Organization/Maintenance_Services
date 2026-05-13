package com.services.maintenance.dto;

import com.services.maintenance.enums.MaintenanceType;

import java.math.BigDecimal;
import java.util.UUID;


public record MaintenanceRequestDTO(

        String plate,

        UUID scheduleId,

        MaintenanceType maintenanceType,

        String mechanicalWorkshop,

        BigDecimal cost,

        String observations

) {
}
