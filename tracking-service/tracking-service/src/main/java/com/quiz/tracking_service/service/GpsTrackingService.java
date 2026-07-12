package com.quiz.tracking_service.service;

import com.quiz.tracking_service.dto.GpsLocationResponse;
import com.quiz.tracking_service.dto.GpsUpdateRequest;
import com.quiz.tracking_service.dto.LivePositionResponse;
import com.quiz.tracking_service.dto.TripResponse;
import com.quiz.tracking_service.dto.VehicleResponse;
import com.quiz.tracking_service.entity.GpsLocation;
import com.quiz.tracking_service.repository.GpsLocationRepository;
import com.quiz.tracking_service.repository.LivePositionRepository;
import com.quiz.tracking_service.websocket.TrackingWebSocketPublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
public class GpsTrackingService {

    private static final Set<String> TRACKABLE_TRIP_STATUSES = Set.of(
            "IN_PROGRESS", "ACTIVE", "STARTED", "ON_TRIP"
    );

    private final GpsLocationRepository gpsLocationRepository;
    private final LivePositionRepository livePositionRepository;
    private final TripValidationService tripValidationService;
    private final LiveMapService liveMapService;
    private final TrackingWebSocketPublisher webSocketPublisher;
    private final int gpsIntervalSeconds;
    private final ConcurrentHashMap<Long, LocalDateTime> lastGpsUpdateByTrip = new ConcurrentHashMap<>();

    public GpsTrackingService(GpsLocationRepository gpsLocationRepository,
                              LivePositionRepository livePositionRepository,
                              TripValidationService tripValidationService,
                              LiveMapService liveMapService,
                              TrackingWebSocketPublisher webSocketPublisher,
                              @Value("${tracking.gps-interval-seconds:3}") int gpsIntervalSeconds) {
        this.gpsLocationRepository = gpsLocationRepository;
        this.livePositionRepository = livePositionRepository;
        this.tripValidationService = tripValidationService;
        this.liveMapService = liveMapService;
        this.webSocketPublisher = webSocketPublisher;
        this.gpsIntervalSeconds = gpsIntervalSeconds;
    }

    public GpsLocationResponse ingestGps(GpsUpdateRequest request, String authToken) {
        TripResponse trip = tripValidationService.validateTrip(request.tripId(), authToken);
        validateTripIsTrackable(trip);
        validateVehicleMatch(trip, request.vehicleId());
        enforceGpsInterval(request.tripId());

        LocalDateTime recordedAt = request.recordedAt() != null ? request.recordedAt() : LocalDateTime.now();
        Long driverId = request.driverId() != null ? request.driverId() : trip.driverId();

        GpsLocation location = new GpsLocation(
                request.tripId(),
                request.vehicleId(),
                driverId,
                request.latitude(),
                request.longitude(),
                request.speed(),
                request.heading(),
                recordedAt
        );

        GpsLocation saved = gpsLocationRepository.save(location);
        lastGpsUpdateByTrip.put(request.tripId(), recordedAt);

        LivePositionResponse livePosition = liveMapService.upsertLivePosition(
                request.tripId(),
                request.vehicleId(),
                driverId,
                request.latitude(),
                request.longitude(),
                request.speed(),
                request.heading(),
                trip.status(),
                authToken,
                recordedAt
        );

        webSocketPublisher.publishLiveMapUpdate(livePosition);
        return mapToResponse(saved);
    }

    public void clearTripTracking(Long tripId) {
        livePositionRepository.deleteByTripId(tripId);
        lastGpsUpdateByTrip.remove(tripId);
    }

    public GpsLocationResponse mapToResponse(GpsLocation location) {
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

    private void validateTripIsTrackable(TripResponse trip) {
        if (trip.status() == null || !TRACKABLE_TRIP_STATUSES.contains(trip.status().toUpperCase())) {
            throw new IllegalArgumentException("Trip " + trip.id() + " is not in a trackable state: " + trip.status());
        }
    }

    private void validateVehicleMatch(TripResponse trip, Long vehicleId) {
        if (trip.vehicleId() != null && !trip.vehicleId().equals(vehicleId)) {
            throw new IllegalArgumentException("Vehicle ID does not match the assigned trip vehicle");
        }
    }

    private void enforceGpsInterval(Long tripId) {
        LocalDateTime lastUpdate = lastGpsUpdateByTrip.get(tripId);
        if (lastUpdate == null) {
            gpsLocationRepository.findTopByTripIdOrderByRecordedAtDesc(tripId)
                    .ifPresent(location -> lastGpsUpdateByTrip.put(tripId, location.getRecordedAt()));
            lastUpdate = lastGpsUpdateByTrip.get(tripId);
        }

        if (lastUpdate != null) {
            long secondsSinceLast = Duration.between(lastUpdate, LocalDateTime.now()).getSeconds();
            if (secondsSinceLast < gpsIntervalSeconds) {
                throw new IllegalArgumentException(
                        "GPS updates must be at least " + gpsIntervalSeconds + " seconds apart. "
                                + "Retry in " + (gpsIntervalSeconds - secondsSinceLast) + " second(s)."
                );
            }
        }
    }
}
