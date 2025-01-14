package com.anikeeva.traineeship.workplacebooking.dto;

import com.anikeeva.traineeship.workplacebooking.entities.enums.StatusEnum;

import java.time.LocalDateTime;
import java.util.UUID;

public record BookingForAdminDTO (
        UUID id,
        String officeName,
        String workspaceName,
        Short floorNumber,
        Integer workplaceNumber,
        LocalDateTime bookingStart,
        LocalDateTime bookingEnd,
        String equipment,
        StatusEnum status,
        boolean isConfirmed,
        String user
) implements BookingDTO
{}
