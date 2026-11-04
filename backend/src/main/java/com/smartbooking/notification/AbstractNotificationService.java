package com.smartbooking.notification;

import lombok.extern.slf4j.Slf4j;

/**
 * Shared template-method base class: validate, format, then dispatch.
 * Demonstrates inheritance + encapsulation of common notification workflow.
 */
@Slf4j
public abstract class AbstractNotificationService implements NotificationService {

    @Override
    public final void send(NotificationMessage message) {
        if (message == null || message.getRecipient() == null) {
            throw new IllegalArgumentException("Notification recipient is required");
        }
        String payload = format(message);
        dispatch(message, payload);
        log.debug("[{}] notified {}", channel(), message.getRecipient().getEmail());
    }

    protected String format(NotificationMessage message) {
        return "[" + message.getSubject() + "] " + message.getBody();
    }

    protected abstract void dispatch(NotificationMessage message, String payload);
}
