package com.services.maintenance.controller;

import com.services.maintenance.dto.ScheduleRequestDTO;
import com.services.maintenance.dto.ScheduleResponseDTO;
import com.services.maintenance.services.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity<ScheduleResponseDTO> create(@RequestBody ScheduleRequestDTO request) {

        ScheduleResponseDTO scheduleResponseDTO = scheduleService.createSchedule(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(scheduleResponseDTO);

    }

    @GetMapping("/{plate}/plate")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity<List<ScheduleResponseDTO>> findAllSchedulesByPlate(@PathVariable String plate) {

        List<ScheduleResponseDTO> response = scheduleService.getAllScheduleByPlate(plate);

        return ResponseEntity.ok(response);
    }
}
