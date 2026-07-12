package com.quiz.trip_service.dto;

import java.util.List;

public record RouteRequest(
    String source,
    String destination,
    String vehicleType
) {}
