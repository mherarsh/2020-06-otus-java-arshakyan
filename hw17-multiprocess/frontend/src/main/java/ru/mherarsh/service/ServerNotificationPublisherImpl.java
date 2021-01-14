package ru.mherarsh.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ServerNotificationPublisherImpl implements ServerNotificationPublisher {
    private final SimpMessagingTemplate messagingTemplate;

    public ServerNotificationPublisherImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void publish(String description, Object data) {
        messagingTemplate.convertAndSend(description, data);
    }
}
