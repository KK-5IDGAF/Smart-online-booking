package com.smartbooking.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Future-ready email channel. Swap this implementation for SMTP/SendGrid without
 * changing BookingService (polymorphism).
 */
@Slf4j
@Service
public class EmailNotificationService extends AbstractNotificationService {

    @Override
    public NotificationChannel channel() {
        return NotificationChannel.EMAIL;
    }

    @Override
    protected void dispatch(NotificationMessage message, String payload) {
        log.info("EMAIL (stub) -> {}: {}", message.getRecipient().getEmail(), payload);
    }
}
