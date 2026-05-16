package com.services.maintenance.controller;

import com.services.maintenance.dto.*;
import com.services.maintenance.services.MaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/maintenances")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity<List<FinishMaintenanceResponseDTO>> findAllMaintenances() {
        List<FinishMaintenanceResponseDTO> response =
                maintenanceService.getAllMaintenances();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity<FinishMaintenanceResponseDTO> findMaintenanceById(@PathVariable UUID id) {

        FinishMaintenanceResponseDTO response = maintenanceService.getMaintenanceById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{plate}/plate")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity<List<FinishMaintenanceResponseDTO>> findAllMaintenanceByPlate(@PathVariable String plate) {

        List<FinishMaintenanceResponseDTO> response = maintenanceService.getAllMaintenancesByPlate(plate);

        return ResponseEntity.ok(response);
    }


    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity<MaintenanceResponseDTO> create(
            @RequestBody MaintenanceRequestDTO request
    ) {
        MaintenanceResponseDTO response =
                maintenanceService.createMaintenance(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity<Void> update(
            @PathVariable UUID id, @RequestBody FinishMaintenanceRequestDTO request
    ) {
        maintenanceService.finishMaintenance(id,request);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT).build();
    }

}
