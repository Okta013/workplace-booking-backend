package com.anikeeva.traineeship.workplacebooking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record WorkspaceForUserDTO(
        UUID id,
        @NotBlank String name,
        @NotNull Short floorNumber,
        @NotNull Short roomNumber,
        @NotNull @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) UUID officeId
) implements WorkspaceDTO
{}
