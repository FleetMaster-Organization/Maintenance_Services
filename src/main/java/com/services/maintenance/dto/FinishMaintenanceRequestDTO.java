package com.services.maintenance.dto;

import java.time.LocalDate;

public record FinishMaintenanceRequestDTO(
        Double endKm,
        LocalDate endDate,
        String observations

) {
}