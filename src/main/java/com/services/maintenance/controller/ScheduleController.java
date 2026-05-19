package com.services.maintenance.controller;

import com.services.maintenance.dto.ScheduleRequestDTO;
import com.services.maintenance.dto.ScheduleResponseDTO;
import com.services.maintenance.services.ScheduleService;
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

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    String defaultUser = "SYSTEM";


    @Operation(
            summary = "Crear un agendamiento",
            description = "Registra un nuevo agendamiento en el sistema. Solo accesible para administradores.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del agendamiento a crear",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ScheduleRequestDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Agendamiento creado exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ScheduleResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
            }
    )
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMINISTRADOR','ROLE_MECANICO')")
    public ResponseEntity<ScheduleResponseDTO> create(@RequestBody ScheduleRequestDTO request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = (auth != null) ? auth.getName() : defaultUser;


        ScheduleResponseDTO scheduleResponseDTO = scheduleService.createSchedule(request, user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(scheduleResponseDTO);

    }

    @Operation(
            summary = "Obtener agendamientos por placa",
            description = "Retorna todos los agendamientos asociados a una placa de vehículo. Solo accesible para administradores.",
            parameters = @Parameter(name = "plate", description = "Placa del vehículo", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de agendamientos obtenida exitosamente",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ScheduleResponseDTO.class)))),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
            }
    )
    @GetMapping("/{plate}/plate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMINISTRADOR','ROLE_MECANICO')")
    public ResponseEntity<List<ScheduleResponseDTO>> findAllSchedulesByPlate(@PathVariable String plate) {

        List<ScheduleResponseDTO> response = scheduleService.getAllScheduleByPlate(plate);

        return ResponseEntity.ok(response);
    }
}
