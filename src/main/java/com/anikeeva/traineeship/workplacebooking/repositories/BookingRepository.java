package com.anikeeva.traineeship.workplacebooking.repositories;

import com.anikeeva.traineeship.workplacebooking.entities.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, UUID> {
    List<BookingEntity> findByUserIdAndIsConfirmedAndCancellationComment(UUID userId, boolean isConfirmed,
                                                                         String cancellationComment);

    List<BookingEntity> findByIsConfirmedAndCancellationDate(boolean isConfirmed, LocalDateTime cancellationDate);

    @Query("SELECT b FROM BookingEntity b " +
            "WHERE b.userId = :id AND :currentDateTime BETWEEN b.bookingStart AND b.bookingEnd AND b.isConfirmed = true")
    BookingEntity findCurrentBooking(@Param("id") UUID userId,
                                     @Param("currentDateTime") LocalDateTime currentDateTime);

    @Query("SELECT b FROM BookingEntity b WHERE b.workplaceId = :workplaceId AND " +
            "((:dateTimeStart BETWEEN b.bookingStart AND b.bookingEnd) OR " +
            "(:dateTimeEnd BETWEEN b.bookingStart AND b.bookingEnd)) " +
            "AND b.cancellationDate = :cancellationDate")
    List<BookingEntity> findByWorkplaceAndCancellated(@Param("workplaceId") UUID workplaceId,
                                                      @Param("dateTimeStart") LocalDateTime dateTimeStart,
                                                      @Param("dateTimeEnd") LocalDateTime dateTimeEnd,
                                                      @Param("cancellationDate") LocalDateTime cancellationDate);

    @Query("SELECT b FROM BookingEntity b WHERE DATE(b.bookingStart) = :date AND b.cancellationDate = :cancellationDate")
    BookingEntity findByDateAndCancelled(@Param("date") LocalDate date,
                                         @Param("cancellationDate") LocalDateTime cancellationDate);

    @Query(value = """
            SELECT r.* FROM reservations r JOIN workplaces wp ON r.workplace_id = wp.id \
            JOIN workspaces ws ON wp.workspace_id = ws.id JOIN offices o ON ws.office_id = o.id \
            JOIN users u ON r.user_id = u.id WHERE (o.name = :officeName OR ws.name = :workspaceName \
            OR wp.number = :workplaceNumber OR u.full_name = :username)""",
            nativeQuery = true)
    List<BookingEntity> findBookings(@Param("officeName") String officeName,
                                     @Param("workspaceName") String workspaceName,
                                     @Param("workplaceNumber") Integer workplaceNumber,
                                     @Param("username") String username);

    List<BookingEntity> getByCancellationDateNull();

    List<BookingEntity> getByCancellationDateNotNull();
}
