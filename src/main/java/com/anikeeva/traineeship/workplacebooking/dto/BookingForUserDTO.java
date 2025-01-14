package com.anikeeva.traineeship.workplacebooking.dto;

import com.anikeeva.traineeship.workplacebooking.entities.enums.StatusEnum;

import java.time.LocalDateTime;
import java.util.UUID;

public record BookingForUserDTO(
        UUID id,
        String officeName,
        String workspaceName,
        short floorNumber,
        int workplaceNumber,
        LocalDateTime bookingStart,
        LocalDateTime bookingEnd,
        StatusEnum status,
        boolean isConfirmed,
        String equipment
) implements BookingDTO
{}
