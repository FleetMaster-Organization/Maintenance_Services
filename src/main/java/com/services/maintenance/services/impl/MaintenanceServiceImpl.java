package com.services.maintenance.services.impl;

import com.services.maintenance.client.VehicleClient;
import com.services.maintenance.dto.MaintenanceRequestDTO;
import com.services.maintenance.dto.MaintenanceResponseDTO;
import com.services.maintenance.dto.VehicleResponseDTO;
import com.services.maintenance.entity.MaintenancesEntity;
import com.services.maintenance.enums.OperationalStatus;
import com.services.maintenance.mapper.MaintenanceMapper;
import com.services.maintenance.repository.MaintenanceRepository;
import com.services.maintenance.services.MaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MaintenanceServiceImpl
        implements MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final VehicleClient vehicleClient;
    private final MaintenanceMapper maintenanceMapper;

    @Override
    public MaintenanceResponseDTO createMaintenance(
            MaintenanceRequestDTO request
    ) {

        VehicleResponseDTO vehicle =
                vehicleClient.getVehicle(
                        request.vehicleId()
                );

        if(vehicle.operationalStatus()
                == OperationalStatus.ASIGNADO) {

            throw new RuntimeException(
                    "Vehículo asignado"
            );
        }

        MaintenancesEntity maintenance =
                maintenanceMapper.toEntity(request);

        maintenance.setVehicleId(vehicle.id());

        maintenance.setStartDate(LocalDate.now());

        maintenance.setStartKm(vehicle.currentKm());

        maintenance.setCreatedAt(OffsetDateTime.now());

        maintenance.setCreatedBy("SYSTEM");

        maintenanceRepository.save(maintenance);

        vehicleClient.sendVehicleToMaintenance(
                vehicle.id()
        );

        return maintenanceMapper.toDTO(maintenance);
    }
}