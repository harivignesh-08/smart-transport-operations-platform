package com.quiz.tracking_service.dto;

import java.time.LocalDateTime;
import java.util.List;

public record TripReplayResponse(
    Long tripId,
    Long vehicleId,
    Long driverId,
    int totalPoints,
    int replayIntervalMs,
    LocalDateTime startTime,
    LocalDateTime endTime,
    long durationSeconds,
    List<GpsLocationResponse> frames,
    String aiSummary
) {}
