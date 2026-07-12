package com.quiz.trip_service.dto;

import java.util.List;

public record RouteResponse(
    List<String> optimizedRoute,
    Double estimatedDistance,
    Double estimatedTime
) {}
