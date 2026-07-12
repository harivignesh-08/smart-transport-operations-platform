package com.quiz.analytics_service.dto;

import java.time.LocalDateTime;

public record TrackingResponse(
    Long id,
    Long vehicleId,
    Double latitude,
    Double longitude,
    Double speed,
    LocalDateTime timestamp
) {}
