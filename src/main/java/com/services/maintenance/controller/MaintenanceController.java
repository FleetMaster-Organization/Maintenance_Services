package com.services.maintenance.controller;

import com.services.maintenance.dto.*;
import com.services.maintenance.services.MaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/maintenances")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

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
