package com.quiz.fuel_expense_service.repository;

import com.quiz.fuel_expense_service.entity.Expense;
import com.quiz.fuel_expense_service.entity.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByVehicleId(Long vehicleId);

    List<Expense> findByTripId(Long tripId);

    List<Expense> findByVehicleIdAndExpenseDateBetween(Long vehicleId, LocalDateTime from, LocalDateTime to);

    List<Expense> findByExpenseDateBetween(LocalDateTime from, LocalDateTime to);

    List<Expense> findByVehicleIdAndCategory(Long vehicleId, ExpenseCategory category);
}
