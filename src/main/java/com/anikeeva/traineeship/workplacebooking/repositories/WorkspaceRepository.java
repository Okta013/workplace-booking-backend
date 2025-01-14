package com.anikeeva.traineeship.workplacebooking.repositories;

import com.anikeeva.traineeship.workplacebooking.entities.WorkspaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkspaceRepository extends JpaRepository<WorkspaceEntity, UUID> {

    @Query(value = "SELECT ws.* FROM workspaces ws WHERE ws.office_id = :officeId AND " +
            "(ws.name = :name OR ws.floor_number = :floorNumber OR ws.room_number = :roomNumber)", nativeQuery = true)
    List<WorkspaceEntity> findByOfficeIdAndNameOrFloorNumberOrRoomNumber(UUID officeId, String name,
                                                                         Short floorNumber, Short roomNumber);

    List<WorkspaceEntity> findByOfficeIdAndIsDeleted(UUID officeId, Boolean isDeleted);

    List<WorkspaceEntity> findByOfficeId(UUID officeId);

    List<WorkspaceEntity> findByName(String name);

    List<WorkspaceEntity> findByFloorNumber(Short floorNumber);

    List<WorkspaceEntity> findByRoomNumber(Short roomNumber);
    @Query(value = "SELECT ws.* FROM workspaces ws LEFT JOIN workplaces wp ON wp.workspace_id = ws.id " +
            "LEFT JOIN reservations r ON r.workplace_id = ws.id WHERE ws.id = :id", nativeQuery = true)
    WorkspaceEntity getWorkspace(@Param("id") UUID id);

    List<WorkspaceEntity> findByIsDeleted(Boolean isDeleted);
}
