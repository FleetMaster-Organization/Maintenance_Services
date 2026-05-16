package com.services.maintenance.services;

import com.services.maintenance.dto.ScheduleRequestDTO;
import com.services.maintenance.dto.ScheduleResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ScheduleService {

    ScheduleResponseDTO createSchedule(ScheduleRequestDTO scheduleRequestDTO);


    List<ScheduleResponseDTO> getAllScheduleByPlate(String plate);

}
