package com.services.maintenance.repository;

import com.services.maintenance.entity.MaintenancesAuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MaitenanceAuditRepository extends JpaRepository<MaintenancesAuditEntity, UUID> {
}
