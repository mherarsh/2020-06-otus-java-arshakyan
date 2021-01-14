package ru.mherarsh.service;

public interface ServerNotificationPublisher {
    void publish(String description, Object data);
}
