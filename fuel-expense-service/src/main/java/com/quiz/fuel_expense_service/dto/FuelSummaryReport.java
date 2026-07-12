package com.quiz.fuel_expense_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public record FuelSummaryReport(
    Long vehicleId,
    LocalDateTime from,
    LocalDateTime to,
    long totalFuelLogs,
    BigDecimal totalLiters,
    BigDecimal totalFuelCost,
    BigDecimal averageCostPerLiter
) {}
