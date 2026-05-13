package com.services.maintenance.services.impl;

import com.services.maintenance.dto.ScheduleRequestDTO;
import com.services.maintenance.dto.ScheduleResponseDTO;
import com.services.maintenance.entity.ScheduleEntity;
import com.services.maintenance.mapper.ScheduleMapper;
import com.services.maintenance.repository.ScheduleRepository;
import com.services.maintenance.services.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleMapper  scheduleMapper;
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
}
