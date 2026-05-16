package com.services.maintenance.services.impl;

import com.services.maintenance.client.VehicleClient;
import com.services.maintenance.dto.*;
import com.services.maintenance.entity.MaintenancesEntity;
import com.services.maintenance.entity.ScheduleEntity;
import com.services.maintenance.enums.OperationalStatus;
import com.services.maintenance.mapper.MaintenanceMapper;
import com.services.maintenance.repository.MaintenanceRepository;
import com.services.maintenance.repository.ScheduleRepository;
import com.services.maintenance.services.MaintenanceService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MaintenanceServiceImpl
        implements MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final VehicleClient vehicleClient;
    private final MaintenanceMapper maintenanceMapper;
    private final ScheduleRepository scheduleRepository;

    @Override
    public MaintenanceResponseDTO createMaintenance(
            MaintenanceRequestDTO request
    ) {

            VehicleResponseDTO vehicle =
                    vehicleClient.getVehicleByPlate(
                            request.plate()
                    );

            if(vehicle.operationalStatus()
                    == OperationalStatus.ASIGNADO) {

                throw new IllegalArgumentException(
                        "El Vehículo se encuentra asignado a un conductor"
                );
            }

            if(vehicle.operationalStatus()
                    == OperationalStatus.EN_MANTENIMIENTO) {
                throw new IllegalArgumentException(
                        "El vehiculo ya se encuentra en mantenimiento"
                );
            }



            MaintenancesEntity maintenance =
                        maintenanceMapper.toEntity(request);

                if(request.scheduleId() != null) {

                    ScheduleEntity schedule =
                            scheduleRepository.findById(request.scheduleId())
                                    .orElseThrow(() ->
                                            new IllegalArgumentException(
                                                    "Schedule no encontrado"
                                            )
                                    );

                    maintenance.setScheduleId(schedule.getId());
                }

                maintenance.setVehicleId(vehicle.id());

                maintenance.setStartDate(LocalDate.now());

                maintenance.setStartKm(vehicle.currentKm());

                maintenance.setVehiclePlate(request.plate());

                maintenance.setCreatedAt(OffsetDateTime.now());


                maintenance.setCreatedBy("SYSTEM");

                maintenanceRepository.save(maintenance);

                vehicleClient.sendVehicleToMaintenance(
                        vehicle.id()
                );

        return maintenanceMapper.toDTO(maintenance);
    }

    @Override
    public List<FinishMaintenanceResponseDTO> getAllMaintenances() {

        List<MaintenancesEntity>  maintenances = maintenanceRepository.findAll();

        return maintenances.stream()
                .map(maintenanceMapper::toFinishDTO)
                .toList();

    }

    @Override
    public List<FinishMaintenanceResponseDTO> getAllMaintenancesByPlate(String plate) {

        try {

            VehicleResponseDTO vehicle =
                    vehicleClient.getVehicleByPlate(plate);

            List<MaintenancesEntity> maintenances =
                    maintenanceRepository.findByVehicleId(vehicle.id());

            return maintenances.stream()
                    .map(maintenanceMapper::toFinishDTO)
                    .toList();

        } catch (FeignException.NotFound e) {

            throw new IllegalArgumentException(
                    "Vehiculo con placa: " + plate + " no encontrado"
            );
        }
    }

    @Override
    public FinishMaintenanceResponseDTO getMaintenanceById(UUID id) {

        MaintenancesEntity maintenances = maintenanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Maintenance no encontrado"));

        return maintenanceMapper.toFinishDTO(maintenances);
    }


    @Override
    public void finishMaintenance(
            UUID id,
            FinishMaintenanceRequestDTO request
    ) {

        MaintenancesEntity maintenance =
                maintenanceRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Mantenimiento no encontrado"
                                )
                        );

        maintenance.setEndDate(LocalDate.now());

        maintenance.setEndKm(request.endKm());

        maintenance.setObservations(request.observations());

        maintenanceRepository.save(maintenance);

        vehicleClient.activateVehicle(
                maintenance.getVehicleId()
        );

    }





}