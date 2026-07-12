package com.quiz.maintenance_service.repository;

import com.quiz.maintenance_service.entity.Repair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairRepository extends JpaRepository<Repair, Long> {
    List<Repair> findByVehicleId(Long vehicleId);
}
