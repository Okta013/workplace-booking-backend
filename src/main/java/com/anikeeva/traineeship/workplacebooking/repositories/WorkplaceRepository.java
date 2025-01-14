package com.anikeeva.traineeship.workplacebooking.repositories;

import com.anikeeva.traineeship.workplacebooking.entities.WorkplaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface WorkplaceRepository extends JpaRepository<WorkplaceEntity, UUID> {
    List<WorkplaceEntity> findByWorkspaceIdAndIsDeleted(UUID workspaceId, Boolean status);

    List<WorkplaceEntity> findByIsDeleted(Boolean status);

    List<WorkplaceEntity> findByWorkspaceId(UUID workspaceId);

    List<WorkplaceEntity> findByNumber(Integer number);

    List<WorkplaceEntity> findByWorkspaceIdAndNumber(UUID workspaceId, Integer number);

    boolean existsByWorkspaceIdAndNumber(UUID workspaceId, int number);

    @Query(value = "SELECT wp.* FROM workplaces wp LEFT JOIN reservations r ON r.workplace_id = wp.id " +
            "WHERE wp.id = ?1", nativeQuery = true)
    WorkplaceEntity getWorkplace(UUID id);

    @Query("SELECT wp FROM WorkplaceEntity wp LEFT JOIN wp.bookings b ON wp.id = b.workplaceId " +
            "AND (b.bookingStart < :dateTimeEnd AND b.bookingEnd > :dateTimeStart) " +
            "WHERE b.id IS NULL")
    List<WorkplaceEntity> findByDate(@Param("dateTimeStart") LocalDateTime dateTimeStart,
                                     @Param("dateTimeEnd") LocalDateTime dateTimeEnd);
}
