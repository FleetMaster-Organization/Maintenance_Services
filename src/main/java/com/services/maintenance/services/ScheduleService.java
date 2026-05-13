package com.services.maintenance.services;

import com.services.maintenance.dto.ScheduleRequestDTO;
import com.services.maintenance.dto.ScheduleResponseDTO;

public interface ScheduleService {

    ScheduleResponseDTO createSchedule(ScheduleRequestDTO scheduleRequestDTO);
}
