package com.anikeeva.traineeship.workplacebooking.repositories;

import com.anikeeva.traineeship.workplacebooking.entities.OfficeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OfficeRepository extends JpaRepository<OfficeEntity, UUID> {
    List<OfficeEntity> findByIsDeleted(Boolean status);

    List<OfficeEntity> findByName(String name);

    @Query(value = "SELECT o.* FROM offices o LEFT JOIN workspaces ws ON ws.office_id = o.id " +
            "LEFT JOIN workplaces wp ON wp.workspace_id = ws.id LEFT JOIN reservations r ON r.workplace_id = wp.id " +
            "WHERE o.id = ?1", nativeQuery = true)
    OfficeEntity getOffice(UUID id);
}
