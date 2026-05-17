package com.services.maintenance.dto;

import java.time.LocalDate;
import java.util.UUID;

public record ScheduleRequestDTO(
         UUID vehicleId,

         UUID scheduleId,

         String vehiclePlate,

         LocalDate scheduledDate,

         String observations,


         String createdBy
) {
}
