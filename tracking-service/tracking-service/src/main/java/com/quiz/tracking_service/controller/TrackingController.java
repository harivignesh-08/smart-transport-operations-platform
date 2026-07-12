package com.quiz.tracking_service.controller;

import com.quiz.tracking_service.dto.GpsLocationResponse;
import com.quiz.tracking_service.dto.GpsUpdateRequest;
import com.quiz.tracking_service.dto.LivePositionResponse;
import com.quiz.tracking_service.dto.RouteHistoryResponse;
import com.quiz.tracking_service.dto.TripReplayResponse;
import com.quiz.tracking_service.service.GpsTrackingService;
import com.quiz.tracking_service.service.LiveMapService;
import com.quiz.tracking_service.service.RouteHistoryService;
import com.quiz.tracking_service.service.TripReplayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/tracking")
public class TrackingController {

    private final GpsTrackingService gpsTrackingService;
    private final LiveMapService liveMapService;
    private final RouteHistoryService routeHistoryService;
    private final TripReplayService tripReplayService;

    public TrackingController(GpsTrackingService gpsTrackingService,
                              LiveMapService liveMapService,
                              RouteHistoryService routeHistoryService,
                              TripReplayService tripReplayService) {
        this.gpsTrackingService = gpsTrackingService;
        this.liveMapService = liveMapService;
        this.routeHistoryService = routeHistoryService;
        this.tripReplayService = tripReplayService;
    }

    @PostMapping("/gps")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<GpsLocationResponse> ingestGps(
            @Valid @RequestBody GpsUpdateRequest request,
            HttpServletRequest httpRequest) {
        GpsLocationResponse response = gpsTrackingService.ingestGps(request, extractAuthHeader(httpRequest));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/live")
    public ResponseEntity<List<LivePositionResponse>> getLiveMap(HttpServletRequest httpRequest) {
        return ResponseEntity.ok(liveMapService.getLiveMap(extractAuthHeader(httpRequest)));
    }

    @GetMapping("/live/trip/{tripId}")
    public ResponseEntity<LivePositionResponse> getLivePositionByTrip(
            @PathVariable Long tripId,
            HttpServletRequest httpRequest) {
        return ResponseEntity.ok(liveMapService.getLivePositionByTrip(tripId, extractAuthHeader(httpRequest)));
    }

    @GetMapping("/trips/{tripId}/route")
    public ResponseEntity<RouteHistoryResponse> getRouteHistory(@PathVariable Long tripId) {
        return ResponseEntity.ok(routeHistoryService.getRouteHistory(tripId));
    }

    @GetMapping("/trips/{tripId}/route/history")
    public ResponseEntity<RouteHistoryResponse> getRouteHistoryBetween(
            @PathVariable Long tripId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(routeHistoryService.getRouteHistoryBetween(tripId, start, end));
    }

    @GetMapping("/trips/{tripId}/replay")
    public ResponseEntity<TripReplayResponse> getTripReplay(
            @PathVariable Long tripId,
            HttpServletRequest httpRequest) {
        return ResponseEntity.ok(tripReplayService.getTripReplay(tripId, extractAuthHeader(httpRequest)));
    }

    @PostMapping("/trips/{tripId}/replay/stream")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<Void> streamTripReplay(
            @PathVariable Long tripId,
            HttpServletRequest httpRequest) {
        String authHeader = extractAuthHeader(httpRequest);
        CompletableFuture.runAsync(() -> tripReplayService.streamTripReplay(tripId, authHeader));
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/trips/{tripId}/live")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> clearTripTracking(@PathVariable Long tripId) {
        gpsTrackingService.clearTripTracking(tripId);
        return ResponseEntity.noContent().build();
    }

    private String extractAuthHeader(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
}
