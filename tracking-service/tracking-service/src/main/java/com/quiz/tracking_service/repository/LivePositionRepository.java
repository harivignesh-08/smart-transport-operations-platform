package com.quiz.tracking_service.repository;

import com.quiz.tracking_service.entity.LivePosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LivePositionRepository extends JpaRepository<LivePosition, Long> {

    Optional<LivePosition> findByTripId(Long tripId);

    List<LivePosition> findByTripStatus(String tripStatus);

    void deleteByTripId(Long tripId);
}
