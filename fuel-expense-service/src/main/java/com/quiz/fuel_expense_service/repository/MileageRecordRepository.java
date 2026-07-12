package com.quiz.fuel_expense_service.repository;

import com.quiz.fuel_expense_service.entity.MileageRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MileageRecordRepository extends JpaRepository<MileageRecord, Long> {

    List<MileageRecord> findByVehicleId(Long vehicleId);

    List<MileageRecord> findByTripId(Long tripId);

    List<MileageRecord> findByVehicleIdAndRecordedAtBetween(Long vehicleId, LocalDateTime from, LocalDateTime to);

    List<MileageRecord> findByRecordedAtBetween(LocalDateTime from, LocalDateTime to);
}
