package com.quiz.driver_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DriverRequest(
    @NotNull(message = "User ID is required")
    Long userId,

    @NotBlank(message = "First name is required")
    String firstName,

    @NotBlank(message = "Last name is required")
    String lastName,

    @NotBlank(message = "Phone is required")
    String phone,

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    String email,

    String status,

    String availabilityStatus,

    Long vehicleId
) {}
