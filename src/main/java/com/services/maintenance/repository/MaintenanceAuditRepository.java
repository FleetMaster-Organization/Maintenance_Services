package com.services.maintenance.repository;

import com.services.maintenance.entity.MaintenancesAuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MaintenanceAuditRepository extends JpaRepository<MaintenancesAuditEntity, UUID> {
}
