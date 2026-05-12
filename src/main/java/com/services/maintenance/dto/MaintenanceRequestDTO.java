package com.services.maintenance.dto;

import com.services.maintenance.enums.MaintenanceType;

import java.math.BigDecimal;


public record MaintenanceRequestDTO(

        String plate,

        MaintenanceType maintenanceType,

        Integer startKm,

        Integer endKm,

        String mechanicalWorkshop,

        BigDecimal cost,

        String observations

) {
}
