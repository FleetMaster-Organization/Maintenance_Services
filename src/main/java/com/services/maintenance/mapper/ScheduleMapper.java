package com.services.maintenance.mapper;

import com.services.maintenance.dto.ScheduleRequestDTO;
import com.services.maintenance.dto.ScheduleResponseDTO;
import com.services.maintenance.entity.ScheduleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScheduleMapper {

    ScheduleEntity toEntity(ScheduleRequestDTO requestDTO);
    ScheduleResponseDTO toDTO(ScheduleEntity responseDTO);

}
