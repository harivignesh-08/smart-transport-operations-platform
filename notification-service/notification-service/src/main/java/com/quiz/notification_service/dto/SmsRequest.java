package com.quiz.notification_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SmsRequest(
    @NotBlank
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format")
    String phoneNumber,
    @NotBlank String message
) {}
