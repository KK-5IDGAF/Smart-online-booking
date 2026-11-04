package com.smartbooking.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Future-ready SMS channel (Twilio/SNS can replace the stub later).
 */
@Slf4j
@Service
public class SmsNotificationService extends AbstractNotificationService {

    @Override
    public NotificationChannel channel() {
        return NotificationChannel.SMS;
    }

    @Override
    protected void dispatch(NotificationMessage message, String payload) {
        log.info("SMS (stub) -> {}: {}", message.getRecipient().getEmail(), payload);
    }
}
