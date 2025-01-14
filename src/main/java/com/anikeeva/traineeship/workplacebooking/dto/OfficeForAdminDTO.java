package com.anikeeva.traineeship.workplacebooking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OfficeForAdminDTO(
        UUID id,
        @NotBlank String address,
        @NotBlank String name,
        @NotNull @JsonProperty(access = JsonProperty.Access.READ_ONLY) Boolean isDeleted
) implements OfficeDTO
{}
