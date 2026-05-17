package com.services.maintenance.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ScheduleResponseDTO(

        UUID id,

        UUID vehicleId,

        String vehiclePlate,

        LocalDate scheduledDate,

        String observations,

        OffsetDateTime createdAt,

        String createdBy
) {
}
