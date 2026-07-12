package com.quiz.tracking_service.service;

import com.quiz.tracking_service.client.TripClient;
import com.quiz.tracking_service.dto.TripResponse;
import org.springframework.stereotype.Service;

@Service
public class TripValidationService {

    private final TripClient tripClient;

    public TripValidationService(TripClient tripClient) {
        this.tripClient = tripClient;
    }

    public TripResponse validateTrip(Long tripId, String authToken) {
        try {
            TripResponse trip = tripClient.getTripById(authToken, tripId);
            if (trip == null) {
                throw new IllegalArgumentException("Trip not found with id: " + tripId);
            }
            return trip;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Unable to validate trip with id: " + tripId);
        }
    }
}
