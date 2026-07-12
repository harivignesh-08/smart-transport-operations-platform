package com.quiz.fuel_expense_service.repository;

import com.quiz.fuel_expense_service.entity.FuelLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FuelLogRepository extends JpaRepository<FuelLog, Long> {

    List<FuelLog> findByVehicleId(Long vehicleId);

    List<FuelLog> findByTripId(Long tripId);

    List<FuelLog> findByVehicleIdAndFilledAtBetween(Long vehicleId, LocalDateTime from, LocalDateTime to);

    List<FuelLog> findByFilledAtBetween(LocalDateTime from, LocalDateTime to);
}
