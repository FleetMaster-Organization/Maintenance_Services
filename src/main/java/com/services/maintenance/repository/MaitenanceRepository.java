package com.services.maintenance.repository;

import com.services.maintenance.entity.MaintenancesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MaitenanceRepository extends JpaRepository<MaintenancesEntity, UUID> {
}
