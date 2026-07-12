package com.quiz.maintenance_service.repository;

import com.quiz.maintenance_service.entity.PredictiveMaintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredictiveMaintenanceRepository extends JpaRepository<PredictiveMaintenance, Long> {
    List<PredictiveMaintenance> findByVehicleIdOrderByPredictionDateDesc(Long vehicleId);
}
