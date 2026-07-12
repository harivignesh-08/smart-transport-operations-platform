package com.quiz.tracking_service.service;

import com.quiz.tracking_service.dto.GpsLocationResponse;
import com.quiz.tracking_service.dto.RouteHistoryResponse;
import com.quiz.tracking_service.entity.GpsLocation;
import com.quiz.tracking_service.repository.GpsLocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RouteHistoryService {

    private final GpsLocationRepository gpsLocationRepository;

    public RouteHistoryService(GpsLocationRepository gpsLocationRepository) {
        this.gpsLocationRepository = gpsLocationRepository;
    }

    public RouteHistoryResponse getRouteHistory(Long tripId) {
        List<GpsLocation> locations = gpsLocationRepository.findByTripIdOrderByRecordedAtAsc(tripId);
        if (locations.isEmpty()) {
            throw new IllegalArgumentException("No GPS history found for trip id: " + tripId);
        }

        List<GpsLocationResponse> route = locations.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        GpsLocation first = locations.getFirst();
        GpsLocation last = locations.getLast();

        return new RouteHistoryResponse(
                tripId,
                first.getVehicleId(),
                first.getDriverId(),
                route.size(),
                first.getRecordedAt(),
                last.getRecordedAt(),
                route
        );
    }

    public RouteHistoryResponse getRouteHistoryBetween(Long tripId, LocalDateTime start, LocalDateTime end) {
        List<GpsLocation> locations = gpsLocationRepository
                .findByTripIdAndRecordedAtBetweenOrderByRecordedAtAsc(tripId, start, end);

        if (locations.isEmpty()) {
            throw new IllegalArgumentException("No GPS history found for trip id: " + tripId + " in the given time range");
        }

        List<GpsLocationResponse> route = locations.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new RouteHistoryResponse(
                tripId,
                locations.getFirst().getVehicleId(),
                locations.getFirst().getDriverId(),
                route.size(),
                locations.getFirst().getRecordedAt(),
                locations.getLast().getRecordedAt(),
                route
        );
    }

    private GpsLocationResponse mapToResponse(GpsLocation location) {
        return new GpsLocationResponse(
                location.getId(),
                location.getTripId(),
                location.getVehicleId(),
                location.getDriverId(),
                location.getLatitude(),
                location.getLongitude(),
                location.getSpeed(),
                location.getHeading(),
                location.getRecordedAt()
        );
    }
}
