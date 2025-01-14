package com.anikeeva.traineeship.workplacebooking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserForUserDTO(
        @NotBlank String fullName,
        @NotNull String phoneNumber,
        @NotNull @Email String email,
        @NotNull boolean passwordNeedsChange
) implements UserDTO
{}