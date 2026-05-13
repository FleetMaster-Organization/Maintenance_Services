package com.services.maintenance.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ScheduleRequestDTO(
         UUID vehicleId,

         UUID scheduleId,

         UUID vehiclePlate,

         LocalDate scheduledDate,

         String observations,


         String createdBy
) {
}
