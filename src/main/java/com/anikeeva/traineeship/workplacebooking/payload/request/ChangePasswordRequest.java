package com.anikeeva.traineeship.workplacebooking.payload.request;

public record ChangePasswordRequest(
        String oldPassword,
        String newPassword
)
{}
