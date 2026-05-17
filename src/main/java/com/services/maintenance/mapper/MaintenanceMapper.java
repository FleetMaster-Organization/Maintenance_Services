package com.services.maintenance.mapper;

import com.services.maintenance.dto.FinishMaintenanceResponseDTO;
import com.services.maintenance.dto.MaintenanceRequestDTO;
import com.services.maintenance.dto.MaintenanceResponseDTO;
import com.services.maintenance.entity.MaintenancesEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MaintenanceMapper {

    @Mapping(source = "plate", target = "vehiclePlate")
    MaintenancesEntity toEntity(MaintenanceRequestDTO maintenanceRequestDTO);

    @Mapping(source = "vehiclePlate", target = "plate")
    MaintenanceResponseDTO toDTO(MaintenancesEntity maintenanceEntity);

    @Mapping(source = "vehiclePlate", target = "plate")
    FinishMaintenanceResponseDTO toFinishDTO(MaintenancesEntity maintenanceEntity);

}
