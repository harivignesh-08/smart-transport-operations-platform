package com.quiz.auth_service.dto;

public record JwtResponse(
    String token,
    String type,
    Long expiresIn
) {
    public JwtResponse(String token, Long expiresIn) {
        this(token, "Bearer", expiresIn);
    }
}
