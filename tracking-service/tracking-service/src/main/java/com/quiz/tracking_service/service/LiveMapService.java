package com.quiz.tracking_service.service;

import com.quiz.tracking_service.client.VehicleClient;
import com.quiz.tracking_service.dto.LivePositionResponse;
import com.quiz.tracking_service.dto.VehicleResponse;
import com.quiz.tracking_service.entity.LivePosition;
import com.quiz.tracking_service.repository.LivePositionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LiveMapService {

    private final LivePositionRepository livePositionRepository;
    private final VehicleClient vehicleClient;

    public LiveMapService(LivePositionRepository livePositionRepository, VehicleClient vehicleClient) {
        this.livePositionRepository = livePositionRepository;
        this.vehicleClient = vehicleClient;
    }

    public LivePositionResponse upsertLivePosition(Long tripId, Long vehicleId, Long driverId,
                                                   Double latitude, Double longitude, Double speed,
                                                   Double heading, String tripStatus, String authToken,
                                                   LocalDateTime lastUpdated) {
        LivePosition position = livePositionRepository.findByTripId(tripId)
                .orElse(new LivePosition());

        position.setTripId(tripId);
        position.setVehicleId(vehicleId);
        position.setDriverId(driverId);
        position.setLatitude(latitude);
        position.setLongitude(longitude);
        position.setSpeed(speed);
        position.setHeading(heading);
        position.setTripStatus(tripStatus);
        position.setLastUpdated(lastUpdated);

        LivePosition saved = livePositionRepository.save(position);
        return mapToResponse(saved, authToken);
    }

    @Transactional(readOnly = true)
    public List<LivePositionResponse> getLiveMap(String authToken) {
        return livePositionRepository.findAll().stream()
                .map(position -> mapToResponse(position, authToken))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LivePositionResponse getLivePositionByTrip(Long tripId, String authToken) {
        LivePosition position = livePositionRepository.findByTripId(tripId)
                .orElseThrow(() -> new IllegalArgumentException("No live position found for trip id: " + tripId));
        return mapToResponse(position, authToken);
    }

    public LivePositionResponse mapToResponse(LivePosition position, String authToken) {
        String vehicleLabel = resolveVehicleLabel(position.getVehicleId(), authToken);
        return new LivePositionResponse(
                position.getTripId(),
                position.getVehicleId(),
                position.getDriverId(),
                position.getLatitude(),
                position.getLongitude(),
                position.getSpeed(),
                position.getHeading(),
                position.getTripStatus(),
                vehicleLabel,
                position.getLastUpdated()
        );
    }

    private String resolveVehicleLabel(Long vehicleId, String authToken) {
        if (vehicleId == null || authToken == null) {
            return null;
        }
        try {
            VehicleResponse vehicle = vehicleClient.getVehicleById(authToken, vehicleId);
            return vehicle.licensePlate() + " - " + vehicle.make() + " " + vehicle.model();
        } catch (Exception ex) {
            return "Vehicle #" + vehicleId;
        }
    }
}
