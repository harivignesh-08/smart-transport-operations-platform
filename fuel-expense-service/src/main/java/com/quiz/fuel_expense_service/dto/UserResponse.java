package com.quiz.fuel_expense_service.dto;

import java.util.List;

public record UserResponse(
    Long id,
    String username,
    String email,
    List<String> roles
) {}
