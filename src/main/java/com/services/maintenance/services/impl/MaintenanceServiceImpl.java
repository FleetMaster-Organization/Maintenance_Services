package com.services.maintenance.services.impl;

import com.services.maintenance.client.VehicleClient;
import com.services.maintenance.dto.*;
import com.services.maintenance.dto.ActivateVehicleRequestDTO;
import com.services.maintenance.entity.MaintenancesAuditEntity;
import com.services.maintenance.entity.MaintenancesEntity;
import com.services.maintenance.entity.ScheduleEntity;
import com.services.maintenance.enums.OperationalStatus;
import com.services.maintenance.mapper.MaintenanceMapper;
import com.services.maintenance.repository.MaintenanceAuditRepository;
import com.services.maintenance.repository.MaintenanceRepository;
import com.services.maintenance.repository.ScheduleRepository;
import com.services.maintenance.services.MaintenanceService;
import com.services.maintenance.exception.BusinessRuleException;
import com.services.maintenance.exception.ResourceNotFoundException;
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
    private final MaintenanceAuditRepository maintenanceAuditRepository;

    @Override
    public MaintenanceResponseDTO createMaintenance(
            MaintenanceRequestDTO request, String createdBy
    ) {

            VehicleResponseDTO vehicle =
                    vehicleClient.getVehicleByPlate(
                            request.plate()
                    );

            if(vehicle.operationalStatus()
                    == OperationalStatus.ASIGNADO) {

                throw new BusinessRuleException(
                        "El Vehículo se encuentra asignado a un conductor"
                );
            }

            if(vehicle.operationalStatus()
                    == OperationalStatus.EN_MANTENIMIENTO) {
                throw new BusinessRuleException(
                        "El vehiculo ya se encuentra en mantenimiento"
                );
            }



            MaintenancesEntity maintenance =
                        maintenanceMapper.toEntity(request);

                if(request.scheduleId() != null) {

                    ScheduleEntity schedule =
                            scheduleRepository.findById(request.scheduleId())
                                    .orElseThrow(() ->
                                            new ResourceNotFoundException(
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


                maintenance.setCreatedBy(createdBy);

                maintenanceRepository.save(maintenance);

                saveAudit(maintenance.getId(), "CREATE", "ALL", null,
                        maintenance.getVehiclePlate(), maintenance.getCreatedBy());

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

            throw new ResourceNotFoundException(
                    "Vehiculo con placa: " + plate + " no encontrado"
            );
        }
    }

    @Override
    public FinishMaintenanceResponseDTO getMaintenanceById(UUID id) {

        MaintenancesEntity maintenances = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance no encontrado"));

        return maintenanceMapper.toFinishDTO(maintenances);
    }


    @Override
    public void finishMaintenance(
            UUID id,
            FinishMaintenanceRequestDTO request, String modifiedBy
    ) {

        MaintenancesEntity maintenance =
                maintenanceRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Mantenimiento no encontrado"
                                )
                        );

        maintenance.setEndDate(LocalDate.now());

        maintenance.setEndKm(request.endKm());

        maintenance.setObservations(request.observations());

        if (request.nextScheduledDate() != null) {
            maintenance.setNextScheduledDate(request.nextScheduledDate());
        }

        maintenanceRepository.save(maintenance);

        saveAudit(maintenance.getId(), "FINISH", "endDate", null, maintenance.getEndDate().toString(), modifiedBy);
        saveAudit(maintenance.getId(), "FINISH", "endKm", null, String.valueOf(maintenance.getEndKm()), modifiedBy);
        saveAudit(maintenance.getId(), "FINISH", "observations", null, maintenance.getObservations(), modifiedBy);

        if (request.nextScheduledDate() != null) {

            if (request.nextScheduledDate().isBefore(LocalDate.now())) {
                throw new BusinessRuleException(
                        "La fecha de la próxima cita no puede ser pasada"
                );
            }

            ScheduleEntity schedule = ScheduleEntity.builder()
                    .vehicleId(maintenance.getVehicleId())
                    .vehiclePlate(maintenance.getVehiclePlate())
                    .scheduledDate(request.nextScheduledDate())
                    .observations("Próxima cita agendada al cerrar mantenimiento")
                    .createdAt(OffsetDateTime.now())
                    .createdBy(modifiedBy)
                    .build();

            scheduleRepository.save(schedule);

            saveAudit(maintenance.getId(), "SCHEDULE_NEXT", "nextScheduledDate",
                    null, request.nextScheduledDate().toString(), modifiedBy);
        }

        vehicleClient.activateVehicle(
                maintenance.getVehicleId(),
                new ActivateVehicleRequestDTO(maintenance.getEndKm())
        );

    }

    private void saveAudit(UUID maintenanceId, String actionType, String field, String oldValue, String newValue, String modifiedBy) {
        MaintenancesAuditEntity audit = MaintenancesAuditEntity.builder()
                .maintenanceId(maintenanceId)
                .actionType(actionType)
                .modifiedField(field)
                .oldValue(oldValue)
                .newValue(newValue)
                .modifiedBy(modifiedBy)
                .modifiedAt(OffsetDateTime.now())
                .build();
        maintenanceAuditRepository.save(audit);
    }
}