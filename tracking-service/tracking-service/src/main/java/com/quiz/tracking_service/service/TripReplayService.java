package com.quiz.tracking_service.service;

import com.quiz.tracking_service.client.AIClient;
import com.quiz.tracking_service.dto.GpsLocationResponse;
import com.quiz.tracking_service.dto.RouteAnalysisRequest;
import com.quiz.tracking_service.dto.RouteAnalysisResponse;
import com.quiz.tracking_service.dto.RouteHistoryResponse;
import com.quiz.tracking_service.dto.TripReplayResponse;
import com.quiz.tracking_service.websocket.TrackingWebSocketPublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TripReplayService {

    private final RouteHistoryService routeHistoryService;
    private final AIClient aiClient;
    private final TrackingWebSocketPublisher webSocketPublisher;
    private final int gpsIntervalSeconds;

    public TripReplayService(RouteHistoryService routeHistoryService,
                             AIClient aiClient,
                             TrackingWebSocketPublisher webSocketPublisher,
                             @Value("${tracking.gps-interval-seconds:3}") int gpsIntervalSeconds) {
        this.routeHistoryService = routeHistoryService;
        this.aiClient = aiClient;
        this.webSocketPublisher = webSocketPublisher;
        this.gpsIntervalSeconds = gpsIntervalSeconds;
    }

    public TripReplayResponse getTripReplay(Long tripId, String authToken) {
        RouteHistoryResponse history = routeHistoryService.getRouteHistory(tripId);
        List<GpsLocationResponse> frames = history.route();

        LocalDateTime startTime = history.startTime();
        LocalDateTime endTime = history.endTime();
        long durationSeconds = Duration.between(startTime, endTime).getSeconds();

        double totalDistanceKm = calculateTotalDistanceKm(frames);
        double averageSpeed = durationSeconds > 0
                ? totalDistanceKm / (durationSeconds / 3600.0)
                : 0.0;

        String aiSummary = fetchAiSummary(tripId, history.vehicleId(), frames.size(), totalDistanceKm, averageSpeed, authToken);

        return new TripReplayResponse(
                tripId,
                history.vehicleId(),
                history.driverId(),
                frames.size(),
                gpsIntervalSeconds * 1000,
                startTime,
                endTime,
                durationSeconds,
                frames,
                aiSummary
        );
    }

    public void streamTripReplay(Long tripId, String authToken) {
        TripReplayResponse replay = getTripReplay(tripId, authToken);
        for (GpsLocationResponse frame : replay.frames()) {
            webSocketPublisher.publishReplayFrame(tripId, new com.quiz.tracking_service.dto.LivePositionResponse(
                    frame.tripId(),
                    frame.vehicleId(),
                    frame.driverId(),
                    frame.latitude(),
                    frame.longitude(),
                    frame.speed(),
                    frame.heading(),
                    "REPLAY",
                    null,
                    frame.recordedAt()
            ));
            try {
                Thread.sleep(replay.replayIntervalMs());
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private String fetchAiSummary(Long tripId, Long vehicleId, int pointCount,
                                  double totalDistanceKm, double averageSpeed, String authToken) {
        try {
            RouteAnalysisResponse analysis = aiClient.analyzeRoute(
                    authToken,
                    new RouteAnalysisRequest(tripId, vehicleId, pointCount, totalDistanceKm, averageSpeed)
            );
            return analysis.summary();
        } catch (Exception ex) {
            return "Route covered " + String.format("%.2f", totalDistanceKm)
                    + " km with " + pointCount + " GPS points.";
        }
    }

    private double calculateTotalDistanceKm(List<GpsLocationResponse> frames) {
        double total = 0.0;
        for (int i = 1; i < frames.size(); i++) {
            GpsLocationResponse previous = frames.get(i - 1);
            GpsLocationResponse current = frames.get(i);
            total += haversineKm(
                    previous.latitude(), previous.longitude(),
                    current.latitude(), current.longitude()
            );
        }
        return total;
    }

    private double haversineKm(double lat1, double lon1, double lat2, double lon2) {
        final int earthRadiusKm = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadiusKm * c;
    }
}
