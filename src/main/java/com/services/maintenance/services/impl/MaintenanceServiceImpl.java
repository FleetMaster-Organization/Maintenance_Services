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

            ScheduleEntity schedule =
                    scheduleRepository.findById(request.scheduleId())
                            .orElseThrow(() ->
                                    new IllegalArgumentException( "No se encuentra el cronograma no encontrado" ));


            MaintenancesEntity maintenance =
                        maintenanceMapper.toEntity(request);



                maintenance.setScheduleId(schedule.getId());

                maintenance.setVehicleId(vehicle.id());

                maintenance.setStartDate(LocalDate.now());

                maintenance.setStartKm(vehicle.currentKm());

                maintenance.setVehiclePlate(vehicle.plate());

                maintenance.setCreatedAt(OffsetDateTime.now());


                maintenance.setCreatedBy("SYSTEM");

                maintenanceRepository.save(maintenance);

                vehicleClient.sendVehicleToMaintenance(
                        vehicle.id()
                );

            return maintenanceMapper.toDTO(maintenance);
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