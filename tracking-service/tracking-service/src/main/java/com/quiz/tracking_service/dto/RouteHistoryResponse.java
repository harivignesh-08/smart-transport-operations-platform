package com.quiz.tracking_service.dto;

import java.time.LocalDateTime;
import java.util.List;

public record RouteHistoryResponse(
    Long tripId,
    Long vehicleId,
    Long driverId,
    int totalPoints,
    LocalDateTime startTime,
    LocalDateTime endTime,
    List<GpsLocationResponse> route
) {}
