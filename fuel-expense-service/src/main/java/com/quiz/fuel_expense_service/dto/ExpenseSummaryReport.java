package com.quiz.fuel_expense_service.dto;

import com.quiz.fuel_expense_service.entity.ExpenseCategory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public record ExpenseSummaryReport(
    Long vehicleId,
    LocalDateTime from,
    LocalDateTime to,
    long totalExpenses,
    BigDecimal totalAmount,
    Map<ExpenseCategory, BigDecimal> amountByCategory
) {}
