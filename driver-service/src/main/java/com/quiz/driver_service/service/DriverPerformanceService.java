package com.quiz.driver_service.service;

import com.quiz.driver_service.dto.PerformanceResponse;
import com.quiz.driver_service.dto.PerformanceUpdateRequest;
import com.quiz.driver_service.entity.Driver;
import com.quiz.driver_service.entity.DriverPerformance;
import com.quiz.driver_service.repository.DriverPerformanceRepository;
import com.quiz.driver_service.repository.DriverRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DriverPerformanceService {

    private final DriverPerformanceRepository performanceRepository;
    private final DriverRepository driverRepository;

    public DriverPerformanceService(DriverPerformanceRepository performanceRepository,
                                    DriverRepository driverRepository) {
        this.performanceRepository = performanceRepository;
        this.driverRepository = driverRepository;
    }

    @Transactional(readOnly = true)
    public PerformanceResponse getPerformanceByDriver(Long driverId) {
        if (!driverRepository.existsById(driverId)) {
            throw new IllegalArgumentException("Driver not found with id: " + driverId);
        }

        DriverPerformance performance = performanceRepository.findByDriverId(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Performance record not found for driver id: " + driverId));

        return mapToResponse(performance);
    }

    public PerformanceResponse recordTripOutcome(Long driverId, PerformanceUpdateRequest request) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found with id: " + driverId));

        DriverPerformance performance = performanceRepository.findByDriverId(driverId)
                .orElseGet(() -> performanceRepository.save(new DriverPerformance(driver)));

        int completedDelta = request.completedTripsDelta() != null ? request.completedTripsDelta() : 0;
        int cancelledDelta = request.cancelledTripsDelta() != null ? request.cancelledTripsDelta() : 0;
        int tripDelta = completedDelta + cancelledDelta;

        if (tripDelta <= 0) {
            throw new IllegalArgumentException("At least one trip outcome delta must be provided");
        }

        performance.setTotalTrips(performance.getTotalTrips() + tripDelta);
        performance.setCompletedTrips(performance.getCompletedTrips() + completedDelta);
        performance.setCancelledTrips(performance.getCancelledTrips() + cancelledDelta);

        if (request.tripRating() != null && completedDelta > 0) {
            double currentTotal = performance.getAverageRating() * (performance.getCompletedTrips() - completedDelta);
            performance.setAverageRating((currentTotal + request.tripRating() * completedDelta) / performance.getCompletedTrips());
        }

        if (request.onTime() != null && completedDelta > 0) {
            int previousCompleted = performance.getCompletedTrips() - completedDelta;
            double previousOnTimeCount = performance.getOnTimePercentage() * previousCompleted / 100.0;
            double newOnTimeCount = previousOnTimeCount + (request.onTime() ? completedDelta : 0);
            performance.setOnTimePercentage(newOnTimeCount * 100.0 / performance.getCompletedTrips());
        }

        if (request.distanceKmDelta() != null) {
            performance.setTotalDistanceKm(performance.getTotalDistanceKm() + request.distanceKmDelta());
        }

        DriverPerformance updated = performanceRepository.save(performance);
        return mapToResponse(updated);
    }

    public PerformanceResponse mapToResponse(DriverPerformance performance) {
        return new PerformanceResponse(
                performance.getId(),
                performance.getDriver().getId(),
                performance.getTotalTrips(),
                performance.getCompletedTrips(),
                performance.getCancelledTrips(),
                performance.getAverageRating(),
                performance.getOnTimePercentage(),
                performance.getTotalDistanceKm(),
                performance.getLastUpdated()
        );
    }
}
