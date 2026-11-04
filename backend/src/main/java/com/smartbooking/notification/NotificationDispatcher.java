package com.smartbooking.notification;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Composition: owns a collection of NotificationService implementations and
 * fans out using polymorphism (each implementation's send() is invoked).
 */
@Component
public class NotificationDispatcher {

    private final List<NotificationService> channels;

    public NotificationDispatcher(List<NotificationService> channels) {
        this.channels = channels;
    }

    public void notifyAllChannels(NotificationMessage message) {
        channels.forEach(channel -> channel.send(message));
    }

    public void notifyChannel(NotificationChannel target, NotificationMessage message) {
        channels.stream()
                .filter(service -> service.supports(target))
                .forEach(service -> service.send(message));
    }
}
