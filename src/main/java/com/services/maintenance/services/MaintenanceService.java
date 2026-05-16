package com.services.maintenance.services;

import com.services.maintenance.dto.FinishMaintenanceRequestDTO;
import com.services.maintenance.dto.FinishMaintenanceResponseDTO;
import com.services.maintenance.dto.MaintenanceRequestDTO;
import com.services.maintenance.dto.MaintenanceResponseDTO;

import java.util.List;
import java.util.UUID;

public interface MaintenanceService {

    MaintenanceResponseDTO createMaintenance(MaintenanceRequestDTO maintenanceRequestDTO);

    List<FinishMaintenanceResponseDTO> getAllMaintenances();

    List<FinishMaintenanceResponseDTO> getAllMaintenancesByPlate(String plate);

    FinishMaintenanceResponseDTO getMaintenanceById(UUID id);

    void finishMaintenance(UUID id, FinishMaintenanceRequestDTO finishMaintenanceRequestDTO);
}
