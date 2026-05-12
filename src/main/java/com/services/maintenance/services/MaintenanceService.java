package com.services.maintenance.services;

import com.services.maintenance.dto.MaintenanceRequestDTO;
import com.services.maintenance.dto.MaintenanceResponseDTO;

public interface MaintenanceService {

    MaintenanceResponseDTO createMaintenance(MaintenanceRequestDTO maintenanceRequestDTO);


}
