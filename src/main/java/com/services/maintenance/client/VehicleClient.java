package com.services.maintenance.client;

import com.services.maintenance.dto.VehicleResponseDTO;
import com.services.maintenance.enums.OperationalStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "vehicle-services")
public interface VehicleClient {

    @GetMapping("/vehicles")
    List<VehicleResponseDTO> getVehiclesByOperationalStatus(
            @RequestParam OperationalStatus operationalStatus
    );
}
