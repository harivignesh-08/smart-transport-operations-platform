package com.quiz.driver_service.repository;

import com.quiz.driver_service.entity.DriverPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverPerformanceRepository extends JpaRepository<DriverPerformance, Long> {

    Optional<DriverPerformance> findByDriverId(Long driverId);
}
