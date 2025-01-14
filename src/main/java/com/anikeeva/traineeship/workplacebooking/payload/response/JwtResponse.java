package com.anikeeva.traineeship.workplacebooking.payload.response;

import java.util.List;

public record JwtResponse(
        String token,
        String email,
        List<String> roles,
        String type
) {
    public JwtResponse(String token, String email, List<String> roles) {
        this(token, email, roles, "Bearer");
    }
}