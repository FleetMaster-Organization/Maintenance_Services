package com.services.maintenance.controller;

import com.services.maintenance.dto.*;
import com.services.maintenance.services.MaintenanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/maintenances")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    String defaultUser = "SYSTEM";


    @Operation(
            summary = "Obtener todos los mantenimientos",
            description = "Retorna la lista completa de mantenimientos registrados. Solo accesible para administradores.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de mantenimientos obtenida exitosamente",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = FinishMaintenanceResponseDTO.class)))),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
            }
    )
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMINISTRADOR','ROLE_MECANICO')")
    public ResponseEntity<List<FinishMaintenanceResponseDTO>> findAllMaintenances() {
        List<FinishMaintenanceResponseDTO> response =
                maintenanceService.getAllMaintenances();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtener mantenimiento por ID",
            description = "Retorna un mantenimiento específico según su UUID. Solo accesible para administradores.",
            parameters = @Parameter(name = "id", description = "UUID del mantenimiento", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Mantenimiento encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = FinishMaintenanceResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Mantenimiento no encontrado", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMINISTRADOR','ROLE_MECANICO')")
    public ResponseEntity<FinishMaintenanceResponseDTO> findMaintenanceById(@PathVariable UUID id) {

        FinishMaintenanceResponseDTO response = maintenanceService.getMaintenanceById(id);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtener mantenimientos por placa",
            description = "Retorna todos los mantenimientos asociados a una placa de vehículo. Solo accesible para administradores.",
            parameters = @Parameter(name = "plate", description = "Placa del vehículo", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de mantenimientos por placa obtenida exitosamente",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = FinishMaintenanceResponseDTO.class)))),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
            }
    )
    @GetMapping("/{plate}/plate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMINISTRADOR','ROLE_MECANICO')")
    public ResponseEntity<List<FinishMaintenanceResponseDTO>> findAllMaintenanceByPlate(@PathVariable String plate) {

        List<FinishMaintenanceResponseDTO> response = maintenanceService.getAllMaintenancesByPlate(plate);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Crear un mantenimiento",
            description = "Registra un nuevo mantenimiento en el sistema. Solo accesible para administradores.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del mantenimiento a crear",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MaintenanceRequestDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Mantenimiento creado exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MaintenanceResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
            }
    )
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMINISTRADOR','ROLE_MECANICO')")
    public ResponseEntity<MaintenanceResponseDTO> create(
            @RequestBody MaintenanceRequestDTO request
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = (auth != null) ? auth.getName() : defaultUser;

        MaintenanceResponseDTO response =
                maintenanceService.createMaintenance(request,user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Finalizar un mantenimiento",
            description = "Actualiza el estado de un mantenimiento existente para marcarlo como finalizado. Solo accesible para administradores.",
            parameters = @Parameter(name = "id", description = "UUID del mantenimiento a finalizar", required = true),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de finalización del mantenimiento",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FinishMaintenanceRequestDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Mantenimiento finalizado exitosamente", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Mantenimiento no encontrado", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
            }
    )
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMINISTRADOR','ROLE_MECANICO')")
    public ResponseEntity<Void> update(
            @PathVariable UUID id, @RequestBody FinishMaintenanceRequestDTO request
    ) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = (auth != null) ? auth.getName() : defaultUser;

        maintenanceService.finishMaintenance(id,request,user);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT).build();
    }

}
