package com.services.maintenance.client;

import com.services.maintenance.dto.ActivateVehicleRequestDTO;
import com.services.maintenance.dto.VehicleResponseDTO;
import com.services.maintenance.enums.OperationalStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.UUID;

@FeignClient(name = "vehicle-services")
public interface VehicleClient {


    @GetMapping("/vehicles/{id}")
    VehicleResponseDTO getVehicle(
            @PathVariable("id") UUID id
    );

    @GetMapping("/vehicles/{plate}/plate")
    VehicleResponseDTO getVehicleByPlate(
            @PathVariable("plate")  String plate
    );

    @PatchMapping("/vehicles/{id}/send-maintenance")
    void sendVehicleToMaintenance(
            @PathVariable("id") UUID id
    );

    @PatchMapping("/vehicles/{id}/activate")
    void activateVehicle(
            @PathVariable("id") UUID id,
            @RequestBody ActivateVehicleRequestDTO request
    );

}
