package com.quiz.tracking_service.websocket;

import com.quiz.tracking_service.dto.LivePositionResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class TrackingWebSocketPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public TrackingWebSocketPublisher(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void publishLiveMapUpdate(LivePositionResponse position) {
        messagingTemplate.convertAndSend("/topic/live-map", position);
        messagingTemplate.convertAndSend("/topic/trip/" + position.tripId(), position);
    }

    public void publishReplayFrame(Long tripId, LivePositionResponse frame) {
        messagingTemplate.convertAndSend("/topic/replay/" + tripId, frame);
    }
}
