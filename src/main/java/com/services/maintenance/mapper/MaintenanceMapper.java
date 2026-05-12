package com.services.maintenance.mapper;

import com.services.maintenance.dto.MaintenanceRequestDTO;
import com.services.maintenance.dto.MaintenanceResponseDTO;
import com.services.maintenance.entity.MaintenancesEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MaintenanceMapper {

    MaintenancesEntity toEntity(MaintenanceRequestDTO maintenanceRequestDTO);
    MaintenanceResponseDTO toDTO(MaintenancesEntity maintenanceEntity);

}
