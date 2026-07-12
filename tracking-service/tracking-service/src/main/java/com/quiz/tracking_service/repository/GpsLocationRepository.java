package com.quiz.tracking_service.repository;

import com.quiz.tracking_service.entity.GpsLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface GpsLocationRepository extends JpaRepository<GpsLocation, Long> {

    List<GpsLocation> findByTripIdOrderByRecordedAtAsc(Long tripId);

    List<GpsLocation> findByTripIdAndRecordedAtBetweenOrderByRecordedAtAsc(
            Long tripId, LocalDateTime start, LocalDateTime end);

    List<GpsLocation> findByVehicleIdOrderByRecordedAtDesc(Long vehicleId);

    Optional<GpsLocation> findTopByTripIdOrderByRecordedAtDesc(Long tripId);

    long countByTripId(Long tripId);
}
