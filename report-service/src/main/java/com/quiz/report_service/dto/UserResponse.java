package com.quiz.report_service.dto;

import java.util.List;

public record UserResponse(
    Long id,
    String username,
    String email,
    List<String> roles
) {}
