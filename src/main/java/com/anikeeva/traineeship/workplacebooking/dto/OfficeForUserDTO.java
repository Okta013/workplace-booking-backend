package com.anikeeva.traineeship.workplacebooking.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OfficeForUserDTO(
        UUID id,
        @NotNull String address,
        @NotNull String name
) implements OfficeDTO
{}
