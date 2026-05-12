package com.services.maintenance.controller;

import com.services.maintenance.dto.MaintenanceRequestDTO;
import com.services.maintenance.dto.MaintenanceResponseDTO;
import com.services.maintenance.dto.VehicleResponseDTO;
import com.services.maintenance.services.MaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        System.out.println(
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getAuthorities()
        );

        MaintenanceResponseDTO response =
                maintenanceService.createMaintenance(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
