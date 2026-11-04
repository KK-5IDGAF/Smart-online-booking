package com.smartbooking.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Current production channel: status updates visible inside the dashboard.
 */
@Slf4j
@Service
public class DashboardNotificationService extends AbstractNotificationService {

    @Override
    public NotificationChannel channel() {
        return NotificationChannel.DASHBOARD;
    }

    @Override
    protected void dispatch(NotificationMessage message, String payload) {
        log.info("DASHBOARD -> {}: {}", message.getRecipient().getEmail(), payload);
    }
}
