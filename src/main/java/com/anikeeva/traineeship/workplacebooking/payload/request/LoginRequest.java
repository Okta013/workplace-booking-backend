package com.anikeeva.traineeship.workplacebooking.payload.request;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull String email,
        @NotNull String password) {
}