package com.quiz.vehicle_service.repository;

import com.quiz.vehicle_service.entity.VehicleDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleDocumentRepository extends JpaRepository<VehicleDocument, Long> {

    List<VehicleDocument> findByVehicleId(Long vehicleId);
}
