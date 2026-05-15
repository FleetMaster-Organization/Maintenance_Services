package com.services.maintenance.repository;

import com.services.maintenance.entity.MaintenancesEntity;
import com.sun.tools.javac.Main;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MaintenanceRepository extends JpaRepository<MaintenancesEntity, UUID> {

    List<MaintenancesEntity>  findByVehicleId(UUID vehicleId);
}
