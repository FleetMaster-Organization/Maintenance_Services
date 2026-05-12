package com.services.maintenance.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.services.maintenance.enums.OperationalStatus;

import java.time.LocalDate;
import java.util.UUID;

public record VehicleResponseDTO (
        UUID id,
        String plate,
        String vin,
        String brand,
        String line,
        Integer modelYear,
        Integer displacementCc,
        String color,
        String service,
        String vehicleClass,
        String bodyType,
        String fuelType,
        String engineNumber,
        Double initialKm,
        Double currentKm,
        OperationalStatus operationalStatus,
        String administrativeStatus,
        LocalDate createdAt,
        LocalDate updatedAt,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String suspensionReason
) {
}
