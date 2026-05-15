package com.services.maintenance.services.impl;

import com.services.maintenance.client.VehicleClient;
import com.services.maintenance.dto.MaintenanceResponseDTO;
import com.services.maintenance.dto.ScheduleRequestDTO;
import com.services.maintenance.dto.ScheduleResponseDTO;
import com.services.maintenance.dto.VehicleResponseDTO;
import com.services.maintenance.entity.MaintenancesEntity;
import com.services.maintenance.entity.ScheduleEntity;
import com.services.maintenance.mapper.ScheduleMapper;
import com.services.maintenance.repository.ScheduleRepository;
import com.services.maintenance.services.ScheduleService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleMapper  scheduleMapper;
    private final VehicleClient vehicleClient;
    private final ScheduleRepository scheduleRepository;

    @Override
    public ScheduleResponseDTO createSchedule(
            ScheduleRequestDTO request
    ) {

        ScheduleEntity schedule =
                scheduleMapper.toEntity(request);

        if(request.scheduledDate()
                .isBefore(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "La fecha programada no puede ser pasada"
            );
        }

        schedule.setCreatedAt(OffsetDateTime.now());

        schedule.setCreatedBy("SYSTEM");

        scheduleRepository.save(schedule);

        return scheduleMapper.toDTO(schedule);
    }

    @Override
    public List<ScheduleResponseDTO> getAllScheduleByPlate(String plate) {

        try {

            VehicleResponseDTO vehicle =
                    vehicleClient.getVehicleByPlate(plate);

            List<ScheduleEntity> schedules =
                    scheduleRepository.findByVehicleId(vehicle.id());

            return schedules.stream()
                    .map(scheduleMapper::toDTO)
                    .toList();

        } catch (FeignException.NotFound e) {

            throw new IllegalArgumentException(
                    "Vehiculo con placa: " + plate + " no encontrado"
            );
        }
    }
}
