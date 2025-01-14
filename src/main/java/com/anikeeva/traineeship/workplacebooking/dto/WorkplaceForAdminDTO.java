package com.anikeeva.traineeship.workplacebooking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record WorkplaceForAdminDTO(
        UUID id,
        @NotNull Integer number,
        @NotBlank String description,
        @NotNull @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) UUID workspaceId,
        @NotNull @JsonProperty(access = JsonProperty.Access.READ_ONLY)  Boolean isDeleted
) implements WorkplaceDTO
{}
