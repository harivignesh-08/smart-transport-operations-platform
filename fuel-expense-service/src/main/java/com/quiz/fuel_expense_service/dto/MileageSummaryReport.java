package com.quiz.fuel_expense_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MileageSummaryReport(
    Long vehicleId,
    LocalDateTime from,
    LocalDateTime to,
    long totalRecords,
    BigDecimal totalDistanceKm,
    BigDecimal averageDistanceKm
) {}
